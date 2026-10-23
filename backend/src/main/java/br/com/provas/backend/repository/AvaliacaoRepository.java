package br.com.provas.backend.repository;

import br.com.provas.backend.model.Avaliacao;
import br.com.provas.backend.model.StatusAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    List<Avaliacao> findByMateriaId(Long materiaId);
    List<Avaliacao> findByStatus(StatusAvaliacao status);
    List<Avaliacao> findByMateriaIdAndStatus(Long materiaId, StatusAvaliacao status);
    List<Avaliacao> findByDataBetweenOrderByDataAsc(LocalDate inicio, LocalDate fim);
}
