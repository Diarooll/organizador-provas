package br.com.provas.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TarefaRequest(
        @NotBlank @Size(max = 255) String descricao
) {
}
