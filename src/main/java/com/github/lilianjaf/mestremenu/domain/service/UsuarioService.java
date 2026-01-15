package com.github.lilianjaf.mestremenu.domain.service;

import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.response.UsuarioResponse;
import com.github.lilianjaf.mestremenu.api.v1.mapper.UsuarioMapper;
import com.github.lilianjaf.mestremenu.domain.exception.NegocioException;
import com.github.lilianjaf.mestremenu.domain.model.Usuario;
import com.github.lilianjaf.mestremenu.domain.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse cadastrar(UsuarioRequest usuarioRequest) {
        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new NegocioException(String.format("Já existe um usuário cadastrado com o e-mail %s", usuarioRequest.getEmail()));
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioRequest);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setDataUltimaAlteracao(LocalDateTime.now());
        
        usuario = usuarioRepository.save(usuario);
        
        return usuarioMapper.toResponse(usuario);
    }
}
