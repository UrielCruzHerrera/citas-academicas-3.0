package mx.uv.dsw.citas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mx.uv.dsw.citas.dto.CitaRequestDTO;
import mx.uv.dsw.citas.dto.CitaResponseDTO;
import mx.uv.dsw.citas.service.CitaService;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService service;

    public CitaController(CitaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> listar(
            @RequestParam Integer idEstudiante) {
        return ResponseEntity.ok(service.listarPorEstudiante(idEstudiante));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CitaResponseDTO> crear(@Valid @RequestBody CitaRequestDTO request) {
        CitaResponseDTO creada = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Integer id,
                                         @RequestParam Integer idUsuario) {
        service.cancelar(id, idUsuario);
        return ResponseEntity.noContent().build();
    }
}