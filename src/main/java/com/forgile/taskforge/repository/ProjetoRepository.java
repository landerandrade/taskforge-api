package com.forgile.taskforge.repository;

import java.util.List;

import com.forgile.taskforge.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    List<Projeto> findByUsuarioEmail(String email);
}
