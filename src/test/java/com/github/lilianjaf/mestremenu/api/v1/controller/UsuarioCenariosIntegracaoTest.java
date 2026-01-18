package com.github.lilianjaf.mestremenu.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.EnderecoRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.SenhaUpdateRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioUpdateRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class UsuarioCenariosIntegracaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario usuarioSalvo;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@teste.com");
        usuario.setLogin("joao");
        usuario.setSenha(passwordEncoder.encode("123456"));
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario.setAtivo(true);
        usuario.setDataUltimaAlteracao(LocalDateTime.now().minusDays(1));
        usuarioSalvo = usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Deve buscar usuário por nome")
    void deveBuscarUsuarioPorNome() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios")
                        .param("nome", "João")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("João Silva")));
    }

    @Test
    @DisplayName("Deve atualizar dados cadastrais via PATCH")
    void deveAtualizarDadosCadastrais() throws Exception {
        UsuarioUpdateRequest updateDto = new UsuarioUpdateRequest();
        updateDto.setNome("João Silva Alterado");
        updateDto.setEmail("joao_alterado@teste.com");
        updateDto.setTipo(TipoUsuario.DONO_DE_RESTAURANTE);
        
        EnderecoRequest endereco = new EnderecoRequest();
        endereco.setLogradouro("Rua Nova");
        endereco.setNumero("456");
        endereco.setBairro("Novo Bairro");
        endereco.setCidade("Nova Cidade");
        endereco.setCep("02000-000");
        endereco.setUf("RJ");
        updateDto.setEndereco(endereco);

        mockMvc.perform(patch("/api/v1/usuarios/" + usuarioSalvo.getLogin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("João Silva Alterado")))
                .andExpect(jsonPath("$.email", is("joao_alterado@teste.com")))
                .andExpect(jsonPath("$.tipo", is("DONO_DE_RESTAURANTE")));

        Usuario usuarioPosUpdate = usuarioRepository.findByLogin(usuarioSalvo.getLogin()).get();
        assertTrue(passwordEncoder.matches("123456", usuarioPosUpdate.getSenha()), "A senha não deveria ter sido alterada");
    }

    @Test
    @DisplayName("Deve realizar atualização parcial via PATCH")
    void deveRealizarAtualizacaoParcial() throws Exception {
        UsuarioUpdateRequest updateDto = new UsuarioUpdateRequest();
        updateDto.setNome("Novo Nome");

        mockMvc.perform(patch("/api/v1/usuarios/" + usuarioSalvo.getLogin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Novo Nome")))
                .andExpect(jsonPath("$.email", is(usuarioSalvo.getEmail())))
                .andExpect(jsonPath("$.tipo", is(usuarioSalvo.getTipo().name())));

        Usuario usuarioPosUpdate = usuarioRepository.findByLogin(usuarioSalvo.getLogin()).get();
        assertEquals("Novo Nome", usuarioPosUpdate.getNome());
        assertEquals(usuarioSalvo.getEmail(), usuarioPosUpdate.getEmail());
    }

    @Test
    @DisplayName("Deve alterar senha com sucesso")
    void deveAlterarSenha() throws Exception {
        SenhaUpdateRequest senhaDto = new SenhaUpdateRequest();
        senhaDto.setSenhaAtual("123456");
        senhaDto.setNovaSenha("654321");

        mockMvc.perform(put("/api/v1/usuarios/" + usuarioSalvo.getLogin() + "/senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(senhaDto)))
                .andExpect(status().isNoContent());

        Usuario usuarioPosUpdate = usuarioRepository.findByLogin(usuarioSalvo.getLogin()).get();
        assertTrue(passwordEncoder.matches("654321", usuarioPosUpdate.getSenha()), "A nova senha deveria estar criptografada no banco");
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar alterar senha com senha atual incorreta")
    void deveRetornarErroSenhaAtualIncorreta() throws Exception {
        SenhaUpdateRequest senhaDto = new SenhaUpdateRequest();
        senhaDto.setSenhaAtual("errada");
        senhaDto.setNovaSenha("654321");

        mockMvc.perform(put("/api/v1/usuarios/" + usuarioSalvo.getLogin() + "/senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(senhaDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail", is("Login ou senha inválidos")));
    }

    @Test
    @DisplayName("Deve retornar erro ao buscar por campo não suportado")
    void deveRetornarErroAoBuscarPorCampoNaoSuportado() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios")
                        .param("email", "joao@teste.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Regra de negócio violada")))
                .andExpect(jsonPath("$.detail", is("O único parâmetro de busca permitido é 'nome'")));
    }

    @Test
    @DisplayName("Deve excluir usuário")
    void deveExcluirUsuario() throws Exception {
        mockMvc.perform(delete("/api/v1/usuarios/" + usuarioSalvo.getLogin()))
                .andExpect(status().isNoContent());

        assertTrue(usuarioRepository.findByLogin(usuarioSalvo.getLogin()).isEmpty());
    }
}
