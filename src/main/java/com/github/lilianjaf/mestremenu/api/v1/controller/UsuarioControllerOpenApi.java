package com.github.lilianjaf.mestremenu.api.v1.controller;

import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioUpdateRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ProblemDetail;

import java.util.List;

@Tag(name = "Usuários", description = "Gerencia os usuários do sistema")
public interface UsuarioControllerOpenApi {

    @Operation(summary = "Cadastra um novo usuário",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados do usuário inválidos ou violação de regra de negócio",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    UsuarioResponse cadastrar(UsuarioRequest usuarioRequest);

    @Operation(summary = "Busca usuários por nome",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
            })
    List<UsuarioResponse> buscarPorNome(
            @Parameter(description = "Nome para busca", example = "João") String nome,
            HttpServletRequest request);

    @Operation(summary = "Atualiza os dados de um usuário",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    UsuarioResponse atualizar(
            @Parameter(description = "Login do usuário", example = "joao.silva") String login,
            UsuarioUpdateRequest usuarioUpdateRequest);

    @Operation(summary = "Exclui um usuário",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    void excluir(
            @Parameter(description = "Login do usuário", example = "joao.silva") String login);
}
