package br.com.provas.backend.controller;


import br.com.provas.backend.dto.MateriaRequest;
import br.com.provas.backend.dto.MateriaResponse;
import br.com.provas.backend.service.MateriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/materias")
public class MateriaController {
    private final MateriaService service;

    public MateriaController(MateriaService service){
        this.service = service;
    }

    @GetMapping
    public List<MateriaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public MateriaResponse buscar(@PathVariable Long id){
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<MateriaResponse> criar(@RequestBody @Valid MateriaRequest request){
        MateriaResponse criada = service.criar(request);
        URI location = URI.create("/materias/" + criada.id());
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    public MateriaResponse atualizar(@PathVariable Long id, @RequestBody @Valid MateriaRequest request){
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id){
        service.excluir(id);
    }



}
