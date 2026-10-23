package br.com.provas.backend.dto;

import br.com.provas.backend.model.TipoAvaliacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AvaliacaoRequest(
        @NotBlank @Size(max = 150) String titulo,
        @NotNull TipoAvaliacao tipo,
        @NotNull LocalDate data,
        String conteudo,
        @NotNull Long materiaId
        ) {
}
