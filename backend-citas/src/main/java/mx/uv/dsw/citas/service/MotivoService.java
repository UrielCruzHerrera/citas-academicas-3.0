package mx.uv.dsw.citas.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import mx.uv.dsw.citas.dto.MotivoDTO;
import mx.uv.dsw.citas.entity.Motivo;
import mx.uv.dsw.citas.repository.MotivoRepository;

@Service
public class MotivoService {

    private final MotivoRepository repository;

    public MotivoService(MotivoRepository repository) {
        this.repository = repository;
    }

    public List<MotivoDTO> listarActivos() {
        return repository.findByActivoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MotivoDTO toDTO(Motivo m) {
        return new MotivoDTO(m.getId(), m.getNombre(), m.getDescripcion());
    }
}