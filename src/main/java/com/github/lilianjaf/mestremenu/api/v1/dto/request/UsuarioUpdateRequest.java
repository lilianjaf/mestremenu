package com.github.lilianjaf.mestremenu.api.v1.dto.request;

import com.github.lilianjaf.mestremenu.domain.model.TipoUsuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

public class UsuarioUpdateRequest {

    private String nome;

    @Email(message = "E-mail deve ser um endereço de e-mail")
    private String email;

    private TipoUsuario tipo;

    @Valid
    private EnderecoRequest endereco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public EnderecoRequest getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoRequest endereco) {
        this.endereco = endereco;
    }
}
