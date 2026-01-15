package com.github.lilianjaf.mestremenu.domain.service;

import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioRequest;
import com.github.lilianjaf.mestremenu.domain.exception.NegocioException;
import com.github.lilianjaf.mestremenu.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

@Component
public class UsuarioValidator {

    private final UsuarioRepository usuarioRepository;

    public UsuarioValidator(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void validar(UsuarioRequest usuarioRequest) {
        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new NegocioException(String.format("Já existe um usuário cadastrado com o e-mail %s", usuarioRequest.getEmail()));
        }

        if (usuarioRepository.existsByLogin(usuarioRequest.getLogin())) {
            throw new NegocioException(String.format("Já existe um usuário cadastrado com o login %s", usuarioRequest.getLogin()));
        }
    }
}
