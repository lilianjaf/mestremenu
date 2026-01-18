package com.github.lilianjaf.mestremenu.api.v1.controller;

import com.github.lilianjaf.mestremenu.api.v1.dto.request.LoginRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;

@Tag(name = "Autenticação", description = "Gerencia a autenticação de usuários")
public interface AutenticacaoControllerOpenApi {

    @Operation(summary = "Efetua login na API", description = "Retorna um token JWT para ser usado em requisições subsequentes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de login inválidos",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    TokenResponse login(LoginRequest loginRequest);
}
