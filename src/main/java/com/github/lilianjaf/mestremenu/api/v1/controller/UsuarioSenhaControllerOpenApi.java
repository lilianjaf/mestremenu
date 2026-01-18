package com.github.lilianjaf.mestremenu.api.v1.controller;

import com.github.lilianjaf.mestremenu.api.v1.dto.request.SenhaUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;

@Tag(name = "Usuários")
public interface UsuarioSenhaControllerOpenApi {

    @Operation(summary = "Altera a senha de um usuário",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    void alterarSenha(
            @Parameter(description = "Login do usuário", example = "joao.silva") String login,
            SenhaUpdateRequest senhaUpdateDto);
}
