package br.com.provas.backend.service;

import br.com.provas.backend.dto.AvaliacaoRequest;
import br.com.provas.backend.dto.AvaliacaoResponse;
import br.com.provas.backend.exception.RecursoNaoEncontradoException;
import br.com.provas.backend.model.Avaliacao;
import br.com.provas.backend.model.Materia;
import br.com.provas.backend.model.StatusAvaliacao;
import br.com.provas.backend.repository.AvaliacaoRepository;
import br.com.provas.backend.repository.MateriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository repository;
    private final MateriaRepository materiaRepository;

    public AvaliacaoService(AvaliacaoRepository repository, MateriaRepository materiaRepository) {
        this.repository = repository;
        this.materiaRepository = materiaRepository;
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponse> listar(Long materiaId, StatusAvaliacao status){
        List<Avaliacao> avaliacoes;
        if(materiaId != null && status != null){
            avaliacoes = repository.findByMateriaIdAndStatus(materiaId, status);

        } else if (materiaId != null) {
            avaliacoes = repository.findByMateriaId(materiaId);
        } else if(status != null) {
            avaliacoes = repository.findByStatus(status);
        } else {
            avaliacoes = repository.findAll();
        }
        return avaliacoes.stream().map(this::paraResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponse> proximas(int dias) {
        LocalDate hoje = LocalDate.now();
        return repository.findByDataBetweenOrderByDataAsc(hoje, hoje.plusDays(dias))
                .stream()
                .map(this::paraResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AvaliacaoResponse buscar(Long id){
        return paraResponse(buscarEntidade(id));
    }

    @Transactional
    public AvaliacaoResponse criar(AvaliacaoRequest request){
        Materia materia = buscarMateria(request.materiaId());
        Avaliacao avaliacao = new Avaliacao(request.titulo(), request.tipo(), request.data(), request.conteudo(), materia);
        return paraResponse(repository.save(avaliacao));
    }
    @Transactional
    public AvaliacaoResponse atualizar(Long id, AvaliacaoRequest request){
        Avaliacao avaliacao = buscarEntidade(id);
        avaliacao.setTitulo(request.titulo());
        avaliacao.setTipo(request.tipo());
        avaliacao.setData(request.data());
        avaliacao.setConteudo(request.conteudo());
        avaliacao.setMateria(buscarMateria(request.materiaId()));
        return paraResponse(avaliacao);
    }

    @Transactional
    public AvaliacaoResponse alterarStatus(Long id, StatusAvaliacao status) {
        Avaliacao avaliacao = buscarEntidade(id);
        avaliacao.setStatus(status);
        return paraResponse(avaliacao);
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarEntidade(id));
    }

    private Avaliacao buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Avaliacao " + id + " nao encontrada"));
    }

    private Materia buscarMateria(Long materiaId) {
        return materiaRepository.findById(materiaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Materia " + materiaId + " nao encontrada"));
    }

    private AvaliacaoResponse paraResponse(Avaliacao a) {
        return new AvaliacaoResponse(a.getId(), a.getTitulo(), a.getTipo(), a.getData(), a.getConteudo(), a.getStatus(), a.getMateria().getId(), a.getMateria().getNome());
    }
}