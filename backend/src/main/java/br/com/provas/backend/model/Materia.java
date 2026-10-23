package br.com.provas.backend.model;

import jakarta.persistence.*;

/**
 * Matéria da faculdade (ex.: "Banco de Dados").
 * Uma matéria tem várias avaliações (o lado "muitos" fica em {@link Avaliacao#getMateria()}).
 *
 * @Entity e @Table dizem ao Hibernate que essa classe representa a tabela materia.
 */
@Entity
@Table(name = "materia")
public class Materia {

    // Primary key. Quem gera o número é o Postgres (coluna BIGSERIAL), por isso IDENTITY.
    // O Java não escolhe o id: ele não está no construtor e não tem setId.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Espelha a coluna nome VARCHAR(100) NOT NULL
    @Column(nullable = false, length = 100)
    private String nome;

    // O Hibernate precisa do construtor vazio pra montar o objeto quando lê do banco.
    // É protected pra que o resto do código use o construtor com nome.
    protected Materia() {
    }

    public Materia(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
