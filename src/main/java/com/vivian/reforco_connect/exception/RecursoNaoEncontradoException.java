package com.vivian.reforco_connect.exception;

/**
 * Lançada pelos services quando um id não existe no banco.
 * O GlobalExceptionHandler transforma em HTTP 404.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " com id " + id + " não encontrado(a)");
    }
}
