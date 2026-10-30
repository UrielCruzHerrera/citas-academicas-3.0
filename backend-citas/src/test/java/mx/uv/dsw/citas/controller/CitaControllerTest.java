package mx.uv.dsw.citas.controller;

import mx.uv.dsw.citas.dto.CitaRequestDTO;
import mx.uv.dsw.citas.dto.CitaResponseDTO;
import mx.uv.dsw.citas.exception.ConflictoException;
import mx.uv.dsw.citas.exception.RecursoNoEncontradoException;
import mx.uv.dsw.citas.service.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CitaControllerTest {

    private CitaService citaService;
    private CitaController controller;

    @BeforeEach
    void setUp() {
        citaService = mock(CitaService.class);
        controller = new CitaController(citaService);
    }

    // ============================================
    // POST /api/citas — 201 Created
    // ============================================
    @Test
    void crear_datosValidos_retorna201() {
        CitaRequestDTO request = new CitaRequestDTO();
        request.setIdEstudiante(1);
        request.setIdDisponibilidad(4);
        request.setIdMotivo(1);

        CitaResponseDTO respuestaEsperada = new CitaResponseDTO(
                1, "Ana Estudiante", "Luis Asesor",
                "2026-10-02 09:00-09:30", "Asesoría de proyecto",
                "SOLICITADA", LocalDateTime.now());

        when(citaService.crear(any(CitaRequestDTO.class))).thenReturn(respuestaEsperada);

        ResponseEntity<CitaResponseDTO> respuesta = controller.crear(request);

        assertEquals(201, respuesta.getStatusCode().value());
        assertNotNull(respuesta.getBody());
        assertEquals(1, respuesta.getBody().getId());
        assertEquals("SOLICITADA", respuesta.getBody().getEstado());
        verify(citaService, times(1)).crear(any(CitaRequestDTO.class));
    }

    // ============================================
    // POST /api/citas — 409 Conflict (doble reserva)
    // ============================================
    @Test
    void crear_disponibilidadOcupada_lanzaConflicto() {
        CitaRequestDTO request = new CitaRequestDTO();
        request.setIdEstudiante(1);
        request.setIdDisponibilidad(4);
        request.setIdMotivo(1);

        when(citaService.crear(any(CitaRequestDTO.class)))
                .thenThrow(new ConflictoException("El horario ya fue reservado"));

        ConflictoException ex = assertThrows(
                ConflictoException.class,
                () -> controller.crear(request)
        );

        assertEquals("El horario ya fue reservado", ex.getMessage());
    }

    // ============================================
    // GET /api/citas/{id} — 200 OK
    // ============================================
    @Test
    void obtener_existente_retorna200() {
        CitaResponseDTO respuestaEsperada = new CitaResponseDTO(
                1, "Ana Estudiante", "Luis Asesor",
                "2026-10-02 09:00-09:30", "Asesoría de proyecto",
                "SOLICITADA", LocalDateTime.now());

        when(citaService.obtenerPorId(1)).thenReturn(respuestaEsperada);

        ResponseEntity<CitaResponseDTO> respuesta = controller.obtener(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(1, respuesta.getBody().getId());
    }

    // ============================================
    // GET /api/citas/{id} — 404 Not Found
    // ============================================
    @Test
    void obtener_inexistente_lanzaNoEncontrado() {
        when(citaService.obtenerPorId(9999))
                .thenThrow(new RecursoNoEncontradoException("Cita no encontrada: 9999"));

        RecursoNoEncontradoException ex = assertThrows(
                RecursoNoEncontradoException.class,
                () -> controller.obtener(9999)
        );

        assertTrue(ex.getMessage().contains("9999"));
    }

    // ============================================
    // DELETE /api/citas/{id} — 204 No Content
    // ============================================
    @Test
    void cancelar_existente_retorna204() {
        doNothing().when(citaService).cancelar(1, 1);

        ResponseEntity<Void> respuesta = controller.cancelar(1, 1);

        assertEquals(204, respuesta.getStatusCode().value());
        assertNull(respuesta.getBody());
        verify(citaService, times(1)).cancelar(1, 1);
    }

    // ============================================
    // DELETE /api/citas/{id} — 409 (ya cancelada)
    // ============================================
    @Test
    void cancelar_yaCancelada_lanzaConflicto() {
        doThrow(new ConflictoException("No se puede cancelar en estado CANCELADA"))
                .when(citaService).cancelar(1, 1);

        ConflictoException ex = assertThrows(
                ConflictoException.class,
                () -> controller.cancelar(1, 1)
        );

        assertTrue(ex.getMessage().contains("CANCELADA"));
    }

    // ============================================
    // GET /api/citas?idEstudiante=1 — 200 OK (lista)
    // ============================================
    @Test
    void listar_retornaListaDeCitas() {
        CitaResponseDTO cita = new CitaResponseDTO(
                1, "Ana", "Luis", "2026-10-02 09:00-09:30",
                "Asesoría", "SOLICITADA", LocalDateTime.now());

        when(citaService.listarPorEstudiante(1)).thenReturn(List.of(cita));

        ResponseEntity<List<CitaResponseDTO>> respuesta =
                controller.listar(1);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(1, respuesta.getBody().size());
        assertEquals(1, respuesta.getBody().get(0).getId());
    }
}