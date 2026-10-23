package br.com.provas.backend.dto;

public record TarefaResponse(
        Long id,
        String descricao,
        boolean concluida,
        Long avaliacaoId
) {
}
