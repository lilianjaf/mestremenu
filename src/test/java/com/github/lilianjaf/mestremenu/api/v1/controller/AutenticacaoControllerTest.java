package com.github.lilianjaf.mestremenu.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.LoginRequest;
import com.github.lilianjaf.mestremenu.core.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class AutenticacaoControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private TokenService tokenService;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("Deve retornar token ao realizar login com sucesso")
    void deveRetornarTokenAoRealizarLoginComSucesso() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("admin");
        loginRequest.setSenha("123");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("admin");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.gerarToken("admin")).thenReturn("mock-token");

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-token"));
    }

    @Test
    @DisplayName("Deve retornar 401 ao realizar login com credenciais inválidas")
    void deveRetornar401AoRealizarLoginComCredenciaisInvalidas() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("errado");
        loginRequest.setSenha("errada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Login ou senha inválidos"));

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Credenciais inválidas"))
                .andExpect(jsonPath("$.detail").value("Login ou senha inválidos"));
    }
}
