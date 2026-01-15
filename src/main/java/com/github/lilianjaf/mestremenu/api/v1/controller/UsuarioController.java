package com.github.lilianjaf.mestremenu.api.v1.controller;

import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.response.UsuarioResponse;
import com.github.lilianjaf.mestremenu.domain.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController("usuarioControllerV1")
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse cadastrar(@RequestBody @Valid UsuarioRequest usuarioRequest) {
        return usuarioService.cadastrar(usuarioRequest);
    }
}
