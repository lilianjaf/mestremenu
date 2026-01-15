package com.github.lilianjaf.mestremenu.api.v1.mapper;

import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.response.EnderecoResponse;
import com.github.lilianjaf.mestremenu.api.v1.dto.response.UsuarioResponse;
import com.github.lilianjaf.mestremenu.domain.model.Endereco;
import com.github.lilianjaf.mestremenu.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setLogin(request.getLogin());
        usuario.setSenha(request.getSenha());
        usuario.setTipo(request.getTipo());

        if (request.getEndereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setLogradouro(request.getEndereco().getLogradouro());
            endereco.setNumero(request.getEndereco().getNumero());
            endereco.setComplemento(request.getEndereco().getComplemento());
            endereco.setBairro(request.getEndereco().getBairro());
            endereco.setCidade(request.getEndereco().getCidade());
            endereco.setCep(request.getEndereco().getCep());
            endereco.setUf(request.getEndereco().getUf());
            usuario.setEndereco(endereco);
        }

        return usuario;
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setLogin(usuario.getLogin());
        response.setTipo(usuario.getTipo());
        response.setDataUltimaAlteracao(usuario.getDataUltimaAlteracao());

        if (usuario.getEndereco() != null) {
            EnderecoResponse enderecoResponse = new EnderecoResponse();
            enderecoResponse.setLogradouro(usuario.getEndereco().getLogradouro());
            enderecoResponse.setNumero(usuario.getEndereco().getNumero());
            enderecoResponse.setComplemento(usuario.getEndereco().getComplemento());
            enderecoResponse.setBairro(usuario.getEndereco().getBairro());
            enderecoResponse.setCidade(usuario.getEndereco().getCidade());
            enderecoResponse.setCep(usuario.getEndereco().getCep());
            enderecoResponse.setUf(usuario.getEndereco().getUf());
            response.setEndereco(enderecoResponse);
        }

        return response;
    }
}
