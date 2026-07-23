package com.inmobiliaria.inmobiliaria_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaRequest {

    private Long idCliente;
    private Long idPropiedad;
    private LocalDate fechaReserva;
    private String estado;

}