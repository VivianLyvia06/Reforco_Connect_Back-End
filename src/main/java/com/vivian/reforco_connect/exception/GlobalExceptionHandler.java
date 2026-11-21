package com.vivian.reforco_connect.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tratamento global de exceções: em vez de 500 com stack trace, a API
 * devolve um JSON padronizado com o status correto.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - @Valid falhou no @RequestBody (ex.: nome em branco)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(MethodArgumentNotValidException excecao) {
        Map<String, String> camposInvalidos = new LinkedHashMap<>();
        for (FieldError erroDeCampo : excecao.getBindingResult().getFieldErrors()) {
            camposInvalidos.put(erroDeCampo.getField(), erroDeCampo.getDefaultMessage());
        }
        Map<String, Object> corpo = montarCorpo(HttpStatus.BAD_REQUEST, "Dados inválidos");
        corpo.put("campos", camposInvalidos);
        return ResponseEntity.badRequest().body(corpo);
    }

    // 400 - validação feita pelo Hibernate na hora de gravar (rede de segurança)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> tratarViolacaoDeRestricao(ConstraintViolationException excecao) {
        Map<String, String> camposInvalidos = new LinkedHashMap<>();
        for (ConstraintViolation<?> violacao : excecao.getConstraintViolations()) {
            camposInvalidos.put(violacao.getPropertyPath().toString(), violacao.getMessage());
        }
        Map<String, Object> corpo = montarCorpo(HttpStatus.BAD_REQUEST, "Dados inválidos");
        corpo.put("campos", camposInvalidos);
        return ResponseEntity.badRequest().body(corpo);
    }

    // 400 - JSON malformado ou data em formato errado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> tratarJsonInvalido(HttpMessageNotReadableException excecao) {
        Map<String, Object> corpo = montarCorpo(HttpStatus.BAD_REQUEST,
                "JSON inválido ou campo em formato incorreto (data esperada: 2026-11-25T14:30:00)");
        return ResponseEntity.badRequest().body(corpo);
    }

    // 404 - id inexistente
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarNaoEncontrado(RecursoNaoEncontradoException excecao) {
        Map<String, Object> corpo = montarCorpo(HttpStatus.NOT_FOUND, excecao.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo);
    }

    // 409 - o banco recusou a operação (FK, NOT NULL, tamanho de coluna...)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> tratarIntegridade(DataIntegrityViolationException excecao) {
        Map<String, Object> corpo = montarCorpo(HttpStatus.CONFLICT,
                "A operação viola uma restrição de integridade do banco de dados");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(corpo);
    }

    private Map<String, Object> montarCorpo(HttpStatus status, String mensagem) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", status.value());
        corpo.put("erro", status.getReasonPhrase());
        corpo.put("mensagem", mensagem);
        return corpo;
    }
}
