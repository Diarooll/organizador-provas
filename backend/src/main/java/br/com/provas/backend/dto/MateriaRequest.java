package br.com.provas.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MateriaRequest(
        @NotBlank @Size(max = 100) String nome
) {
}
