package com.forgile.taskforge.dto;

import java.time.LocalDateTime;

public record ProjetoResponse(
        Long id,
        String nome,
        String descricao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao) {

    public static ProjetoResponse from(com.forgile.taskforge.model.Projeto projeto) {
        return new ProjetoResponse(
                projeto.getId(),
                projeto.getNome(),
                projeto.getDescricao(),
                projeto.getDataCriacao(),
                projeto.getDataAtualizacao()
        );
    }
}
