package mx.uv.dsw.citas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.dsw.citas.entity.CambioEstado;

public interface CambioEstadoRepository extends JpaRepository<CambioEstado, Integer> {
    List<CambioEstado> findByCitaId(Integer idCita);
}