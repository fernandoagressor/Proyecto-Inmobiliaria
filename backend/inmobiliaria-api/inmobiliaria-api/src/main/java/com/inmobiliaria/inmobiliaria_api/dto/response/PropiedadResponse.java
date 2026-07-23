package com.inmobiliaria.inmobiliaria_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropiedadResponse {

    private Long idPropiedad;

    private String codigo;

    private String titulo;

    private String descripcion;

    private String direccion;

    private Double valor;

    private String estado;

    private Boolean activo;

}