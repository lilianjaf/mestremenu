package com.github.lilianjaf.mestremenu.api.exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.github.lilianjaf.mestremenu.domain.exception.CredenciaisInvalidasException;
import com.github.lilianjaf.mestremenu.domain.exception.NegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private record Field(String name, String userMessage) {}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        log.error("Ocorreu um erro interno inesperado: {}", ex.getMessage(), ex);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status,
                "Ocorreu um erro interno inesperado no sistema. Tente novamente e se o problema persistir, entre em contato com o administrador.");
        problemDetail.setTitle("Erro de servidor");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/erro-de-servidor"));

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({BadCredentialsException.class, CredenciaisInvalidasException.class})
    public ProblemDetail handleCredenciaisInvalidas(Exception ex, WebRequest request) {
        log.warn("Tentativa de login com credenciais inválidas: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Login ou senha inválidos");
        problemDetail.setTitle("Credenciais inválidas");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/credenciais-invalidas"));
        return problemDetail;
    }

    @ExceptionHandler(NegocioException.class)
    public ProblemDetail handleNegocio(NegocioException ex, WebRequest request) {
        log.warn("Violação de regra de negócio: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Regra de negócio violada");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/erro-de-negocio"));
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ex.getRootCause();

        if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Corpo da requisição inválido");
        problemDetail.setTitle("Corpo da requisição inválido");

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String path = joinPath(ex.getPath());

        String detail = String.format("O corpo da requisição contém uma propriedade desconhecida: '%s'. " +
                "Certifique-se de que os campos enviados estão de acordo com a documentação.", path);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle("Corpo da requisição inválido");
        problemDetail.setType(URI.create("https://mestremenu.com.br/erros/corpo-da-requisicao-invalido"));

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
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

        log.warn("Dados inválidos na requisição: {}", fields);

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }
}
