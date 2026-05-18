package com.forgile.taskforge.repository;

import java.util.List;

import com.forgile.taskforge.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByUsuarioEmail(String email);
}
