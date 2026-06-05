package com.forgile.taskforge.dto;

import com.forgile.taskforge.model.enums.TarefaStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TarefaRequest(
        @NotBlank @Size(max = 255) String titulo,
        String descricao,
        Long projetoId,
        TarefaStatus status) {

}
