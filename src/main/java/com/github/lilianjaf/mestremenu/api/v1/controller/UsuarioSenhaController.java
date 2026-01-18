package com.github.lilianjaf.mestremenu.api.v1.controller;

import com.github.lilianjaf.mestremenu.api.v1.dto.request.SenhaUpdateRequest;
import com.github.lilianjaf.mestremenu.domain.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios/{login}/senha")
public class UsuarioSenhaController implements UsuarioSenhaControllerOpenApi {

    private final UsuarioService usuarioService;

    public UsuarioSenhaController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable String login, @RequestBody @Valid SenhaUpdateRequest senhaUpdateDto) {
        usuarioService.alterarSenha(login, senhaUpdateDto);
    }
}
