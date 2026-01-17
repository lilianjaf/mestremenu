package com.github.lilianjaf.mestremenu.domain.service;

import com.github.lilianjaf.mestremenu.api.v1.dto.request.SenhaUpdateRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioUpdateRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.response.UsuarioResponse;
import com.github.lilianjaf.mestremenu.api.v1.mapper.UsuarioMapper;
import com.github.lilianjaf.mestremenu.domain.exception.CredenciaisInvalidasException;
import com.github.lilianjaf.mestremenu.domain.exception.NegocioException;
import com.github.lilianjaf.mestremenu.domain.model.Usuario;
import com.github.lilianjaf.mestremenu.domain.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioValidator usuarioValidator;

    private static final ZoneId ZONE_SP = ZoneId.of("America/Sao_Paulo");

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, 
                          PasswordEncoder passwordEncoder, UsuarioValidator usuarioValidator) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.usuarioValidator = usuarioValidator;
    }

    @Transactional
    public UsuarioResponse cadastrar(UsuarioRequest usuarioRequest) {
        usuarioValidator.validar(usuarioRequest);

        Usuario usuario = usuarioMapper.toEntity(usuarioRequest);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setDataUltimaAlteracao(LocalDateTime.now(ZONE_SP));
        
        usuario = usuarioRepository.save(usuario);
        
        return usuarioMapper.toResponse(usuario);
    }

    public List<UsuarioResponse> buscarPorNome(String nome, Map<String, String[]> parameterMap) {
        validarParametrosBusca(parameterMap);

        if (nome == null) {
            return listarTodos();
        }

        return usuarioRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(usuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void validarParametrosBusca(Map<String, String[]> parameterMap) {
        if (parameterMap.size() > 1 || (parameterMap.size() == 1 && !parameterMap.containsKey("nome"))) {
            throw new NegocioException("O único parâmetro de busca permitido é 'nome'");
        }
    }

    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse buscarPorLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .map(usuarioMapper::toResponse)
                .orElseThrow(CredenciaisInvalidasException::new);
    }

    @Transactional
    public UsuarioResponse atualizar(String login, UsuarioUpdateRequest usuarioUpdateRequest) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NegocioException("Usuário não encontrado"));

        usuarioValidator.validarAtualizacao(usuarioUpdateRequest, usuario.getId());
        usuarioMapper.copyToEntity(usuarioUpdateRequest, usuario);
        usuario.setDataUltimaAlteracao(LocalDateTime.now(ZONE_SP));
        
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public void alterarSenha(String login, SenhaUpdateRequest senhaUpdateDto) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(senhaUpdateDto.getSenhaAtual(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        usuario.setSenha(passwordEncoder.encode(senhaUpdateDto.getNovaSenha()));
        usuario.setDataUltimaAlteracao(LocalDateTime.now(ZONE_SP));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluir(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new NegocioException("Usuário não encontrado"));
        
        usuarioRepository.delete(usuario);
    }
}
