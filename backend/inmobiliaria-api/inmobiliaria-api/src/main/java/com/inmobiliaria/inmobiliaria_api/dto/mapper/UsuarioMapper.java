package com.inmobiliaria.inmobiliaria_api.dto.mapper;

import com.inmobiliaria.inmobiliaria_api.dto.response.UsuarioResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(Usuario usuario) {

        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getCorreo(),
                usuario.getActivo(),
                usuario.getPersona().getIdPersona(),
                usuario.getRol().getNombre()
        );

    }

}