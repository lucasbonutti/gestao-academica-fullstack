package com.controle.monitoria_api.exceptions;

import com.controle.monitoria_api.service.exceptions.RecursoNaoEncontradoException;
import com.controle.monitoria_api.service.exceptions.ValidacaoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<StandardError> recursoNaoEncontrado(RecursoNaoEncontradoException e, HttpServletRequest request) {
        String erro = "Recurso não encontrado!";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), erro, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<StandardError> validacao(ValidacaoException e, HttpServletRequest request) {
        String erro = "Regra de negócio violada!";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), erro, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        String erro = "Dados inválidos!";
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError(Instant.now(), status.value(), erro, "Um ou mais campos estão inváidos!", request.getRequestURI());
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            err.addErro(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> credenciaisInvalidas(BadCredentialsException e, HttpServletRequest request) {
        String erro = "Credenciais inválidas!";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String mensagem = "Usuário ou senha incorretos!";
        StandardError err = new StandardError(Instant.now(), status.value(), erro, mensagem, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<StandardError> erroNaAutenticacao(AuthenticationException e, HttpServletRequest request) {
        String erro = "Erro de autenticação!";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String mensagem = "Credenciais inválidas ou token não informado!";
        StandardError err = new StandardError(Instant.now(), status.value(), erro, mensagem, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> erroGenerico(Exception e, HttpServletRequest request) {
        String erro = "Erro interno do servidor!";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String mensagem = "Ocorreu um erro inesperado. Tente novamente mais tarde.";
        StandardError err = new StandardError(Instant.now(), status.value(), erro, mensagem, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
