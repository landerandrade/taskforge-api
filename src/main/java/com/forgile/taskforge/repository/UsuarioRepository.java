package com.forgile.taskforge.repository;

import java.util.Optional;

import com.forgile.taskforge.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}
