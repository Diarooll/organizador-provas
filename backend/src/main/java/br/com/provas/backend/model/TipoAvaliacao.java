package br.com.provas.backend.model;

/**
 * Tipos de avaliação da FIAP.
 * Gravado no banco como texto (coluna avaliacao.tipo, VARCHAR(30)).
 * Cuidado: renomear um valor quebra as linhas que já estão gravadas com o nome antigo.
 */
public enum TipoAvaliacao {
    PROVA,
    CHECKPOINT,
    GLOBAL_SOLUTION,
    CHALLENGE,
    TRABALHO
}
