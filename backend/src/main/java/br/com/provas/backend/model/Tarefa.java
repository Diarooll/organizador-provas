package br.com.provas.backend.model;

import jakarta.persistence.*;

/**
 * Item de estudo de uma avaliação (ex.: "revisar capítulo 3").
 * Muitas tarefas pertencem a uma {@link Avaliacao}. Apagar a avaliação apaga as tarefas
 * (ON DELETE CASCADE na V1).
 */
@Entity
@Table(name = "tarefa")
public class Tarefa {

    // Primary key gerada pelo Postgres (BIGSERIAL)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // VARCHAR(255) NOT NULL
    @Column(nullable = false, length = 255)
    private String descricao;

    // boolean primitivo nunca é null, combina com BOOLEAN NOT NULL DEFAULT FALSE
    @Column(nullable = false)
    private boolean concluida = false;

    // Muitas tarefas -> uma avaliação. No banco é a FK avaliacao_id.
    // Sem setter: uma tarefa não muda de avaliação.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao;

    // Construtor vazio exigido pelo Hibernate
    protected Tarefa() {
    }

    public Tarefa(String descricao, Avaliacao avaliacao) {
        this.descricao = descricao;
        this.avaliacao = avaliacao;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }
}
