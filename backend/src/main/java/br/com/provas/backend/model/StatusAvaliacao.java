package br.com.provas.backend.model;

/**
 * Situação de uma avaliação.
 * Gravado no banco como texto (coluna avaliacao.status, VARCHAR(20), default 'PENDENTE').
 */
public enum StatusAvaliacao {
    PENDENTE,
    CONCLUIDA
}
