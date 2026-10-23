package br.com.provas.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class TratadorDeErros {
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontrado(RecursoNaoEncontradoException e){
        ErroResponse erro = new ErroResponse(404, e.getMessage(), Map.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> dadosInvalidos(MethodArgumentNotValidException e){
        Map<String, String > campos = new LinkedHashMap<>();

        for(FieldError campo : e.getBindingResult().getFieldErrors()){
            campos.put(campo.getField(), campo.getDefaultMessage());
        }
        ErroResponse erro = new ErroResponse(400, "Dados invalidos", campos);
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> conflito(DataIntegrityViolationException e){
        ErroResponse erro = new ErroResponse(409, "Operacao nao permitida: existem registros ligados a este", Map.of());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }


}
