package br.com.provas.backend.repository;

import br.com.provas.backend.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByAvaliacaoId(Long avaliacaoId);
}
