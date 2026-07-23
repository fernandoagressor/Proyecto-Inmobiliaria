package com.inmobiliaria.inmobiliaria_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponse {

    private Long idCliente;
    private Long idPersona;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String direccion;

}