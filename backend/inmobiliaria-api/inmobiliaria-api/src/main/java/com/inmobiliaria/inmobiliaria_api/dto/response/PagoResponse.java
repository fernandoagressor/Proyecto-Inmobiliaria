package com.inmobiliaria.inmobiliaria_api.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponse {

    private Long idPago;
    private Long idContrato;
    private String nombreCliente;
    private String codigoPropiedad;
    private String estadoContrato;
    private Integer numeroCuota;
    private LocalDate fechaPago;
    private BigDecimal valorPago;
    private String medioPago;
    private String observacion;
    private BigDecimal saldoPendiente;
}