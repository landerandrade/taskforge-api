package com.forgile.taskforge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TarefaRequest(

        @NotBlank
        @Size(max = 255)
        String titulo,

        String descricao) {

}
