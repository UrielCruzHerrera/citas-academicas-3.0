package mx.uv.dsw.citas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.uv.dsw.citas.dto.MotivoDTO;
import mx.uv.dsw.citas.service.MotivoService;

@RestController
@RequestMapping("/api/motivos")
public class MotivoController {

    private final MotivoService service;

    public MotivoController(MotivoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MotivoDTO>> listar() {
        return ResponseEntity.ok(service.listarActivos());
    }
}