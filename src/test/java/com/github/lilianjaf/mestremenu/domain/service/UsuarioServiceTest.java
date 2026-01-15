package com.github.lilianjaf.mestremenu.domain.service;

import com.github.lilianjaf.mestremenu.BaseTest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.EnderecoRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.response.UsuarioResponse;
import com.github.lilianjaf.mestremenu.api.v1.mapper.UsuarioMapper;
import com.github.lilianjaf.mestremenu.domain.exception.NegocioException;
import com.github.lilianjaf.mestremenu.domain.model.TipoUsuario;
import com.github.lilianjaf.mestremenu.domain.model.Usuario;
import com.github.lilianjaf.mestremenu.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest extends BaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioValidator usuarioValidator;

    @Spy
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {
        UsuarioRequest request = criarUsuarioRequest();
        doNothing().when(usuarioValidator).validar(request);
        when(passwordEncoder.encode(any())).thenReturn("encoded-password");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioResponse response = usuarioService.cadastrar(request);

        assertNotNull(response);
        assertEquals(request.getEmail(), response.getEmail());
        assertNotNull(response.getDataUltimaAlteracao());
        verify(usuarioValidator, times(1)).validar(request);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar quando validador falhar")
    void deveLancarExcecaoAoCadastrarQuandoValidadorFalhar() {
        UsuarioRequest request = criarUsuarioRequest();
        doThrow(new NegocioException("Erro de validação")).when(usuarioValidator).validar(request);

        assertThrows(NegocioException.class, () -> usuarioService.cadastrar(request));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    private UsuarioRequest criarUsuarioRequest() {
        UsuarioRequest request = new UsuarioRequest();
        request.setNome("João Silva");
        request.setEmail("joao@teste.com");
        request.setLogin("joao");
        request.setSenha("123456");
        request.setTipo(TipoUsuario.CLIENTE);

        EnderecoRequest endereco = new EnderecoRequest();
        endereco.setLogradouro("Rua A");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setCep("01000-000");
        endereco.setUf("SP");

        request.setEndereco(endereco);
        return request;
    }
}
