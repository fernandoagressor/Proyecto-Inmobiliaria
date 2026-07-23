package com.inmobiliaria.inmobiliaria_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse {

    private Long idUsuario;
    private String correo;
    private Boolean activo;
    private Long idPersona;
    private String rol;

}