package com.github.lilianjaf.mestremenu.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.LoginRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AutenticacaoIntegracaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve autenticar usuário admin cadastrado via migração")
    void deveAutenticarUsuarioAdmin() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("admin");
        loginRequest.setSenha("123");

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setEmail("admin@mestremenu.com.br");
        admin.setLogin("admin");
        admin.setSenha(passwordEncoder.encode("123"));
        admin.setTipo(TipoUsuario.DONO_DE_RESTAURANTE);
        admin.setAtivo(true);
        usuarioRepository.save(admin);
        
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setEmail("joao@teste.com");
        usuario.setLogin("joaosilva");
        usuario.setSenha(passwordEncoder.encode("senha123"));
        usuario.setTipo(TipoUsuario.CLIENTE);
        usuario.setAtivo(true);
        
        usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Deve autenticar usuário cadastrado no banco de dados")
    void deveAutenticarUsuarioNoBanco() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("joaosilva");
        loginRequest.setSenha("senha123");

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Deve falhar ao autenticar com senha incorreta")
    void deveFalharComSenhaIncorreta() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("joaosilva");
        loginRequest.setSenha("senha_errada");

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
