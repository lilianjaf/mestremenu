package com.github.lilianjaf.mestremenu.domain.service;

import com.github.lilianjaf.mestremenu.BaseTest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioRequest;
import com.github.lilianjaf.mestremenu.domain.exception.NegocioException;
import com.github.lilianjaf.mestremenu.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UsuarioValidatorTest extends BaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioValidator usuarioValidator;

    @Test
    @DisplayName("Deve validar com sucesso quando email e login não existem")
    void deveValidarComSucesso() {
        UsuarioRequest request = new UsuarioRequest();
        request.setEmail("teste@teste.com");
        request.setLogin("teste");

        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByLogin(request.getLogin())).thenReturn(false);

        assertDoesNotThrow(() -> usuarioValidator.validar(request));
    }

    @Test
    @DisplayName("Deve lançar exceção quando e-mail já existe")
    void deveLancarExcecaoQuandoEmailExiste() {
        UsuarioRequest request = new UsuarioRequest();
        request.setEmail("existente@teste.com");

        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(NegocioException.class, () -> usuarioValidator.validar(request));
    }

    @Test
    @DisplayName("Deve lançar exceção quando login já existe")
    void deveLancarExcecaoQuandoLoginExiste() {
        UsuarioRequest request = new UsuarioRequest();
        request.setEmail("novo@teste.com");
        request.setLogin("existente");

        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByLogin(request.getLogin())).thenReturn(true);

        assertThrows(NegocioException.class, () -> usuarioValidator.validar(request));
    }
}
