package mx.uv.dsw.citas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.uv.dsw.citas.dto.CitaRequestDTO;
import mx.uv.dsw.citas.dto.CitaResponseDTO;
import mx.uv.dsw.citas.entity.CambioEstado;
import mx.uv.dsw.citas.entity.Cita;
import mx.uv.dsw.citas.entity.Disponibilidad;
import mx.uv.dsw.citas.entity.Motivo;
import mx.uv.dsw.citas.entity.Usuario;
import mx.uv.dsw.citas.exception.ConflictoException;
import mx.uv.dsw.citas.exception.RecursoNoEncontradoException;
import mx.uv.dsw.citas.repository.CambioEstadoRepository;
import mx.uv.dsw.citas.repository.CitaRepository;
import mx.uv.dsw.citas.repository.DisponibilidadRepository;
import mx.uv.dsw.citas.repository.MotivoRepository;
import mx.uv.dsw.citas.repository.UsuarioRepository;

@Service
public class CitaService {

    private final CitaRepository citaRepo;
    private final DisponibilidadRepository dispRepo;
    private final UsuarioRepository usuarioRepo;
    private final MotivoRepository motivoRepo;
    private final CambioEstadoRepository cambioRepo;

    public CitaService(CitaRepository citaRepo,
                       DisponibilidadRepository dispRepo,
                       UsuarioRepository usuarioRepo,
                       MotivoRepository motivoRepo,
                       CambioEstadoRepository cambioRepo) {
        this.citaRepo = citaRepo;
        this.dispRepo = dispRepo;
        this.usuarioRepo = usuarioRepo;
        this.motivoRepo = motivoRepo;
        this.cambioRepo = cambioRepo;
    }

    /** Crea una cita validando doble reserva y registrando la transición. */
    @Transactional
    public CitaResponseDTO crear(CitaRequestDTO request) {
        Disponibilidad disp = dispRepo.findById(request.getIdDisponibilidad())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Disponibilidad no encontrada: " + request.getIdDisponibilidad()));

        // Validación negativa: ¿ya está ocupada?
        if (!"DISPONIBLE".equals(disp.getEstado())) {
            throw new ConflictoException(
                    "El horario seleccionado ya fue reservado. Elija otro.");
        }

        // Validación: ¿ya existe una cita para esa disponibilidad? (defensa extra)
        if (citaRepo.existsByDisponibilidadId(disp.getId())) {
            throw new ConflictoException(
                    "Ya existe una cita registrada para ese horario.");
        }

        Usuario estudiante = usuarioRepo.findById(request.getIdEstudiante())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Estudiante no encontrado: " + request.getIdEstudiante()));

        Motivo motivo = motivoRepo.findById(request.getIdMotivo())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Motivo no encontrado: " + request.getIdMotivo()));

        // 1. Crear cita
        Cita cita = new Cita();
        cita.setEstudiante(estudiante);
        cita.setDisponibilidad(disp);
        cita.setMotivo(motivo);
        cita.setEstado("SOLICITADA");
        cita.setFechaSolicitud(LocalDateTime.now());
        cita.setObservaciones(request.getObservaciones());
        cita = citaRepo.save(cita);

        // 2. Registrar bitácora
        CambioEstado cambio = new CambioEstado();
        cambio.setCita(cita);
        cambio.setEstadoAnterior(null);
        cambio.setEstadoNuevo("SOLICITADA");
        cambio.setUsuario(estudiante);
        cambio.setFechaCambio(LocalDateTime.now());
        cambio.setObservacion("Cita creada por el estudiante");
        cambioRepo.save(cambio);

        // 3. Marcar disponibilidad como OCUPADA
        disp.setEstado("OCUPADO");
        dispRepo.save(disp);

        return toDTO(cita);
    }

    @Transactional(readOnly = true)
    public List<CitaResponseDTO> listarPorEstudiante(Integer idEstudiante) {
        return citaRepo.findByEstudianteId(idEstudiante).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CitaResponseDTO obtenerPorId(Integer id) {
        Cita cita = citaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada: " + id));
        return toDTO(cita);
    }

    /** Cancela una cita: cambia estado, registra bitácora y libera el bloque. */
    @Transactional
    public void cancelar(Integer idCita, Integer idUsuario) {
        Cita cita = citaRepo.findById(idCita)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada: " + idCita));

        String estadoActual = cita.getEstado();
        if (!"SOLICITADA".equals(estadoActual) && !"CONFIRMADA".equals(estadoActual)) {
            throw new ConflictoException(
                    "No se puede cancelar una cita en estado " + estadoActual);
        }

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado: " + idUsuario));

        // 1. Cambiar estado de la cita
        cita.setEstado("CANCELADA");
        citaRepo.save(cita);

        // 2. Bitácora
        CambioEstado cambio = new CambioEstado();
        cambio.setCita(cita);
        cambio.setEstadoAnterior(estadoActual);
        cambio.setEstadoNuevo("CANCELADA");
        cambio.setUsuario(usuario);
        cambio.setFechaCambio(LocalDateTime.now());
        cambio.setObservacion("Cancelada por el usuario");
        cambioRepo.save(cambio);

        // 3. Liberar el bloque
        Disponibilidad disp = cita.getDisponibilidad();
        disp.setEstado("DISPONIBLE");
        dispRepo.save(disp);
    }

    private CitaResponseDTO toDTO(Cita c) {
        String fechaHora = c.getDisponibilidad().getFecha()
                + " " + c.getDisponibilidad().getHoraInicio()
                + "-" + c.getDisponibilidad().getHoraFin();

        return new CitaResponseDTO(
                c.getId(),
                c.getEstudiante().getNombre(),
                c.getDisponibilidad().getAsesor().getNombre(),
                fechaHora,
                c.getMotivo().getNombre(),
                c.getEstado(),
                c.getFechaSolicitud()
        );
    }
}