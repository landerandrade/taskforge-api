package com.forgile.taskforge.repository;

import com.forgile.taskforge.model.Tarefa;
import com.forgile.taskforge.model.enums.TarefaStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    Page<Tarefa> findByUsuarioEmail(String email, Pageable pageable);

    Page<Tarefa> findByUsuarioEmailAndStatus(String email, TarefaStatus status, Pageable pageable);
}
