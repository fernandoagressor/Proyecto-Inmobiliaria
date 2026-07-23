package com.inmobiliaria.inmobiliaria_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContratoResponse {

    private Long idContrato;

    private Long idCliente;
    private String nombreCliente;

    private Long idPropiedad;
    private String codigoPropiedad;

    private BigDecimal valorTotal;
    private BigDecimal cuotaInicial;
    private BigDecimal saldoPendiente;

    private Integer numeroCuotas;

    private LocalDate fechaInicio;

    private String estado;

}