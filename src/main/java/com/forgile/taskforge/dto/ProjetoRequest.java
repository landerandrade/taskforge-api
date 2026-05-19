package com.forgile.taskforge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjetoRequest(
        @NotBlank
        @Size(max = 150)
        String nome,
        String descricao) {

}
