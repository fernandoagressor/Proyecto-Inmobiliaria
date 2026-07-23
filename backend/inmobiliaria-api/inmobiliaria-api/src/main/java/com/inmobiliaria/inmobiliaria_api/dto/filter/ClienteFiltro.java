package com.inmobiliaria.inmobiliaria_api.dto.filter;

import lombok.Data;

@Data
public class ClienteFiltro {

    private String nombre;
    private String correo;
    private String tipoDocumento;
    private String numeroDocumento;

}