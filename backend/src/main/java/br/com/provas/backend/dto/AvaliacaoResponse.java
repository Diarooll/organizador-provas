package br.com.provas.backend.dto;

import br.com.provas.backend.model.StatusAvaliacao;
import br.com.provas.backend.model.TipoAvaliacao;

import java.time.LocalDate;

public record AvaliacaoResponse(
        Long id,
        String titulo,
        TipoAvaliacao tipo,
        LocalDate data,
        String conteudo,
        StatusAvaliacao status,
        Long materiaId,
        String materiaNome
) {
}
