package mx.uv.dsw.citas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.dsw.citas.entity.Motivo;

public interface MotivoRepository extends JpaRepository<Motivo, Integer> {
    List<Motivo> findByActivoTrue();
}