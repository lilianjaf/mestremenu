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
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private record Field(String name, String userMessage) {}

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

        List<Field> fields = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new Field(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.");
        problemDetail.setTitle("Dados inválidos");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/dados-invalidos"));
        problemDetail.setProperty("fields", fields);

        ex.printStackTrace();

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }
}
