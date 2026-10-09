package mx.uv.dsw.citas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.dsw.citas.entity.Cita;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByEstudianteId(Integer idEstudiante);
    boolean existsByDisponibilidadId(Integer idDisponibilidad);
}