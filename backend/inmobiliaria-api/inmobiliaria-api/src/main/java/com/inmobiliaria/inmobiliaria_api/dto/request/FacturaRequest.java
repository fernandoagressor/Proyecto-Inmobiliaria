package com.inmobiliaria.inmobiliaria_api.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaRequest {

    @NotNull(message = "El contrato es obligatorio")
    private Long idContrato;

    @NotNull(message = "El número de factura es obligatorio")
    @Positive(message = "El número de factura debe ser mayor que cero")
    private Integer numeroFactura;

    @NotNull(message = "La fecha de emisión es obligatoria")
    private LocalDate fechaEmision;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @FutureOrPresent(message = "La fecha de vencimiento debe ser hoy o una fecha futura")
    private LocalDate fechaVencimiento;

    @NotNull(message = "El valor de la factura es obligatorio")
    @Positive(message = "El valor de la factura debe ser mayor que cero")
    private BigDecimal valorFactura;
}