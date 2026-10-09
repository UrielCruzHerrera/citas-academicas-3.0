package mx.uv.dsw.citas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.uv.dsw.citas.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}