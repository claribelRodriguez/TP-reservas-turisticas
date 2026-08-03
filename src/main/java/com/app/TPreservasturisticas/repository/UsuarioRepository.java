package com.app.TPreservasturisticas.repository;

import com.app.TPreservasturisticas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
