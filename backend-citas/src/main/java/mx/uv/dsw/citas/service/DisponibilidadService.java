package mx.uv.dsw.citas.service;

import mx.uv.dsw.citas.dto.DisponibilidadDTO;
import mx.uv.dsw.citas.entity.Disponibilidad;
import mx.uv.dsw.citas.repository.DisponibilidadRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisponibilidadService {

    private final DisponibilidadRepository repository;

    public DisponibilidadService(DisponibilidadRepository repository) {
        this.repository = repository;
    }

    public List<DisponibilidadDTO> listarDisponibles() {
        List<Disponibilidad> bloques = repository.findByEstadoAndFechaGreaterThanEqual(
                "DISPONIBLE", LocalDate.now());

        return bloques.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private DisponibilidadDTO toDTO(Disponibilidad d) {
        return new DisponibilidadDTO(
                d.getId(),
                d.getAsesor().getNombre(),
                d.getFecha(),
                d.getHoraInicio(),
                d.getHoraFin(),
                d.getEstado()
        );
    }
}