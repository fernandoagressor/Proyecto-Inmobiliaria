package com.inmobiliaria.inmobiliaria_api.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class PagoRequest {

    @NotNull(message = "El contrato es obligatorio")
    private Long idContrato;

    @NotNull(message = "El número de cuota es obligatorio")
    @Positive(message = "El número de cuota debe ser mayor que cero")
    private Integer numeroCuota;

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDate fechaPago;

    @NotNull(message = "El valor del pago es obligatorio")
    @Positive(message = "El valor del pago debe ser mayor que cero")
    private BigDecimal valorPago;

    @NotBlank(message = "El medio de pago es obligatorio")
    private String medioPago;

    private String observacion;
}