package com.github.lilianjaf.mestremenu.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class UnknownFieldsIntegracaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioSalvo;

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
        usuarioSalvo = usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Deve retornar 400 quando enviar campo desconhecido no UsuarioUpdateRequest")
    void deveRetornar400QuandoEnviarCampoDesconhecido() throws Exception {
        String json = """
                {
                    "nome": "João Silva Alterado",
                    "email": "joao_alterado@teste.com",
                    "endereco": {
                        "logradouro": "Rua A",
                        "numero": "123",
                        "bairro": "Centro",
                        "cidade": "São Paulo",
                        "cep": "01000-000",
                        "uf": "SP"
                    },
                    "campoDesconhecido": "valor"
                }
                """;

        mockMvc.perform(patch("/api/v1/usuarios/" + usuarioSalvo.getLogin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Corpo da requisição inválido")))
                .andExpect(jsonPath("$.detail", containsString("O corpo da requisição contém uma propriedade desconhecida: 'campoDesconhecido'")));
    }
}
