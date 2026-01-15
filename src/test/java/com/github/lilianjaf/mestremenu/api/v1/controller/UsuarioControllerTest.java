package com.github.lilianjaf.mestremenu.api.v1.controller;

import com.github.lilianjaf.mestremenu.BaseTest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.EnderecoRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.response.UsuarioResponse;
import com.github.lilianjaf.mestremenu.domain.model.TipoUsuario;
import com.github.lilianjaf.mestremenu.domain.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UsuarioControllerTest extends BaseTest {

    @Autowired
    @Qualifier("usuarioControllerV1")
    private UsuarioController usuarioController;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve retornar cadastrar usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() throws Exception {
        UsuarioRequest request = criarUsuarioRequest();
        UsuarioResponse response = new UsuarioResponse();
        response.setId(1L);
        response.setNome(request.getNome());
        response.setEmail(request.getEmail());

        when(usuarioService.cadastrar(any(UsuarioRequest.class))).thenReturn(response);

        UsuarioResponse result = usuarioController.cadastrar(request);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(request.getNome(), result.getNome());
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
