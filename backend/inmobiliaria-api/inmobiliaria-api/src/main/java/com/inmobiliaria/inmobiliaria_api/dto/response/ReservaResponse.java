package com.inmobiliaria.inmobiliaria_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaResponse {

    private Long idReserva;
    private Long idCliente;
    private String nombreCliente;
    private Long idPropiedad;
    private String codigoPropiedad;
    private LocalDate fechaReserva;
    private String estado;
    private Boolean activo;

}