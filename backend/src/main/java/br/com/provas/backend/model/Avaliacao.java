package br.com.provas.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Uma avaliação de uma matéria (prova, checkpoint, GS...).
 * Muitas avaliações pertencem a uma {@link Materia}; uma avaliação tem várias {@link Tarefa}s.
 */
@Entity
@Table(name = "avaliacao")
public class Avaliacao {

    // Primary key gerada pelo Postgres (BIGSERIAL)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // VARCHAR(150) NOT NULL
    @Column(nullable = false, length = 150)
    private String titulo;

    // STRING grava o nome do valor ("PROVA"). Sem isso o JPA gravaria a posição (0, 1, 2...)
    // e reordenar o enum mudaria o significado dos dados antigos.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoAvaliacao tipo;

    // DATE NOT NULL: só a data, sem hora
    @Column(nullable = false)
    private LocalDate data;

    // A coluna é TEXT. Sem columnDefinition o Hibernate esperaria VARCHAR(255).
    @Column(columnDefinition = "TEXT")
    private String conteudo;

    // O banco tem DEFAULT 'PENDENTE', mas o Hibernate sempre manda o campo no INSERT.
    // Se fosse null, o NOT NULL quebraria; por isso o default também fica aqui no Java.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAvaliacao status = StatusAvaliacao.PENDENTE;

    // Muitas avaliações -> uma matéria. No banco é a FK materia_id.
    // LAZY: a matéria só é buscada quando alguém chamar getMateria().
    // optional = false: toda avaliação precisa de matéria.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    // Construtor vazio exigido pelo Hibernate
    protected Avaliacao() {
    }

    public Avaliacao(String titulo, TipoAvaliacao tipo, LocalDate data, String conteudo, Materia materia) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.data = data;
        this.conteudo = conteudo;
        this.materia = materia;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TipoAvaliacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoAvaliacao tipo) {
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public StatusAvaliacao getStatus() {
        return status;
    }

    public void setStatus(StatusAvaliacao status) {
        this.status = status;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }
}
