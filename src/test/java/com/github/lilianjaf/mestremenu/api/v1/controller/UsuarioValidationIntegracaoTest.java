package com.github.lilianjaf.mestremenu.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.EnderecoRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioRequest;
import com.github.lilianjaf.mestremenu.domain.model.TipoUsuario;
import com.github.lilianjaf.mestremenu.domain.model.Usuario;
import com.github.lilianjaf.mestremenu.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class UsuarioValidationIntegracaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@teste.com");
        usuario.setLogin("joao");
        usuario.setSenha("123456");
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Deve retornar 400 quando login já existir")
    void deveRetornar400QuandoLoginExistir() throws Exception {
        UsuarioRequest request = criarUsuarioRequest();
        request.setLogin("joao"); // Login já cadastrado no setUp
        request.setEmail("outro@teste.com");

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://mestremenu.com.br/erros/erro-de-negocio")))
                .andExpect(jsonPath("$.title", is("Regra de negócio violada")))
                .andExpect(jsonPath("$.detail", is("Já existe um usuário cadastrado com o login joao")));
    }

    @Test
    @DisplayName("Deve retornar 201 e o formato correto da data de última alteração")
    void deveRetornar201EFormatoDataCorreto() throws Exception {
        UsuarioRequest request = criarUsuarioRequest();
        request.setLogin("novo_usuario");
        request.setEmail("novo@teste.com");

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dataUltimaAlteracao", matchesPattern("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}")));
    }

    private UsuarioRequest criarUsuarioRequest() {
        UsuarioRequest request = new UsuarioRequest();
        request.setNome("Maria Silva");
        request.setEmail("maria@teste.com");
        request.setLogin("maria");
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

    @Test
    @DisplayName("Deve retornar 400 e lista de erros estruturada quando dados forem inválidos")
    void deveRetornar400ComErrosEstruturados() throws Exception {
        UsuarioRequest request = new UsuarioRequest(); // Objeto vazio para disparar erros de validação

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("https://mestremenu.com.br/erros/dados-invalidos")))
                .andExpect(jsonPath("$.title", is("Dados inválidos")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", containsString("Um ou mais campos estão inválidos")))
                .andExpect(jsonPath("$.fields", hasSize(greaterThan(0))));
    }
}
