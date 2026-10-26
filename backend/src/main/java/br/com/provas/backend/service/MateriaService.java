package br.com.provas.backend.service;

import br.com.provas.backend.dto.MateriaRequest;
import br.com.provas.backend.dto.MateriaResponse;
import br.com.provas.backend.exception.RecursoNaoEncontradoException;
import br.com.provas.backend.model.Materia;
import br.com.provas.backend.repository.MateriaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaService {
    private final MateriaRepository repository;

    public MateriaService(MateriaRepository repository){
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MateriaResponse> listar() {
        return repository.findAll().stream()
                .map(this::paraResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MateriaResponse buscar(Long id){
        return paraResponse(buscarEntidade(id));
    }

    @Transactional
    public MateriaResponse criar(MateriaRequest request){
        Materia materia = repository.save(new Materia(request.nome()));
        return paraResponse(materia);
    }

    @Transactional
    public MateriaResponse atualizar(Long id, MateriaRequest request){
        Materia materia = buscarEntidade(id);
        materia.setNome(request.nome());
        return paraResponse(materia);
    }

    @Transactional
    public void excluir(Long id){
        Materia materia = buscarEntidade(id);
        repository.delete(materia);
        repository.flush();
    }

    private Materia buscarEntidade(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Materia " + id + " nao encontrada"));
    }

    private MateriaResponse paraResponse(Materia materia){
        return new MateriaResponse(materia.getId(), materia.getNome());
    }
}
