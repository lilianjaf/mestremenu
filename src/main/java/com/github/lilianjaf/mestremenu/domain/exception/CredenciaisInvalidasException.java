package com.github.lilianjaf.mestremenu.domain.exception;

public class CredenciaisInvalidasException extends NegocioException {
    public CredenciaisInvalidasException() {
        super("Login ou senha inválidos");
    }
}
