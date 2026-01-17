package com.github.lilianjaf.mestremenu.api.v1.mapper;

import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.request.UsuarioUpdateRequest;
import com.github.lilianjaf.mestremenu.api.v1.dto.response.EnderecoResponse;
import com.github.lilianjaf.mestremenu.api.v1.dto.response.UsuarioResponse;
import com.github.lilianjaf.mestremenu.domain.model.Endereco;
import com.github.lilianjaf.mestremenu.domain.model.TipoUsuario;
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
        if (request.isDonoDeRestaurante()) {
            usuario.setTipo(TipoUsuario.DONO_DE_RESTAURANTE);
        } else {
            usuario.setTipo(TipoUsuario.CLIENTE);
        }

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

    public void copyToEntity(UsuarioUpdateRequest request, Usuario usuario) {
        if (request.getNome() != null) {
            usuario.setNome(request.getNome());
        }
        if (request.getEmail() != null) {
            usuario.setEmail(request.getEmail());
        }
        if (request.getTipo() != null) {
            usuario.setTipo(request.getTipo());
        }

        if (request.getEndereco() != null) {
            if (usuario.getEndereco() == null) {
                usuario.setEndereco(new Endereco());
            }
            if (request.getEndereco().getLogradouro() != null) {
                usuario.getEndereco().setLogradouro(request.getEndereco().getLogradouro());
            }
            if (request.getEndereco().getNumero() != null) {
                usuario.getEndereco().setNumero(request.getEndereco().getNumero());
            }
            if (request.getEndereco().getComplemento() != null) {
                usuario.getEndereco().setComplemento(request.getEndereco().getComplemento());
            }
            if (request.getEndereco().getBairro() != null) {
                usuario.getEndereco().setBairro(request.getEndereco().getBairro());
            }
            if (request.getEndereco().getCidade() != null) {
                usuario.getEndereco().setCidade(request.getEndereco().getCidade());
            }
            if (request.getEndereco().getCep() != null) {
                usuario.getEndereco().setCep(request.getEndereco().getCep());
            }
            if (request.getEndereco().getUf() != null) {
                usuario.getEndereco().setUf(request.getEndereco().getUf());
            }
        }
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
