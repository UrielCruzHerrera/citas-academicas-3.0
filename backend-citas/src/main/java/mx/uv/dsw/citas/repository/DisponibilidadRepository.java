package mx.uv.dsw.citas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.dsw.citas.entity.Disponibilidad;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Integer> {
    List<Disponibilidad> findByEstadoAndFechaGreaterThanEqual(String estado, LocalDate fecha);
}