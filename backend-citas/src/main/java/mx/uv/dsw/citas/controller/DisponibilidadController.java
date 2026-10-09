package mx.uv.dsw.citas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.uv.dsw.citas.dto.DisponibilidadDTO;
import mx.uv.dsw.citas.service.DisponibilidadService;

@RestController
@RequestMapping("/api/disponibilidades")
public class DisponibilidadController {

    private final DisponibilidadService service;

    public DisponibilidadController(DisponibilidadService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DisponibilidadDTO>> listar() {
        return ResponseEntity.ok(service.listarDisponibles());
    }
}