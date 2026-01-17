package com.github.lilianjaf.mestremenu.api.v1.controller;

import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioUpdateRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.response.UsuarioResponse;
import com.github.lilianjaf.mestremenu.domain.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<UsuarioResponse> buscarPorNome(@RequestParam(required = false) String nome, HttpServletRequest request) {
        return usuarioService.buscarPorNome(nome, request.getParameterMap());
    }

    @GetMapping("/{login}")
    public UsuarioResponse buscarPorLogin(@PathVariable String login) {
        return usuarioService.buscarPorLogin(login);
    }

    @PatchMapping("/{login}")
    public UsuarioResponse atualizar(@PathVariable String login, @RequestBody @Valid UsuarioUpdateRequest usuarioUpdateRequest) {
        return usuarioService.atualizar(login, usuarioUpdateRequest);
    }

    @DeleteMapping("/{login}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable String login) {
        usuarioService.excluir(login);
    }
}
