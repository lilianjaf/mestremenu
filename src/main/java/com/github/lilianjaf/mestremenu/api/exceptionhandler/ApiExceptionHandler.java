package com.github.lilianjaf.mestremenu.api.exceptionhandler;

import com.github.lilianjaf.mestremenu.domain.exception.NegocioException;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Login ou senha inválidos");
        problemDetail.setTitle("Credenciais inválidas");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/credenciais-invalidas"));
        ex.printStackTrace();
        return problemDetail;
    }

    @ExceptionHandler(NegocioException.class)
    public ProblemDetail handleNegocio(NegocioException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Regra de negócio violada");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/erro-de-negocio"));
        ex.printStackTrace();
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle("Dados inválidos");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/dados-invalidos"));
        ex.printStackTrace();
        
        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }
}
