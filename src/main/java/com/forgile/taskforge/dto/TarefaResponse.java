package com.forgile.taskforge.dto;

import java.time.LocalDateTime;

import com.forgile.taskforge.model.Tarefa;
import com.forgile.taskforge.model.enums.TarefaStatus;

public record TarefaResponse(
        Long id,
        String titulo,
        String descricao,
        TarefaStatus status,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        Long projetoId,
        String projetoNome
) {

    public static TarefaResponse from(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getDataCriacao(),
                tarefa.getDataAtualizacao(),
                tarefa.getProjeto() != null ? tarefa.getProjeto().getId() : null,
                tarefa.getProjeto() != null ? tarefa.getProjeto().getNome() : null
        );
    }
}
