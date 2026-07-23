package com.inmobiliaria.inmobiliaria_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContratoRequest {

    @NotNull(message = "El cliente es obligatorio")
    private Long idCliente;

    @NotNull(message = "La propiedad es obligatoria")
    private Long idPropiedad;

    @NotNull(message = "El valor total es obligatorio")
    @Positive(message = "El valor total debe ser mayor que cero")
    private BigDecimal valorTotal;

    @NotNull(message = "La cuota inicial es obligatoria")
    @PositiveOrZero(message = "La cuota inicial no puede ser negativa")
    private BigDecimal cuotaInicial;

    @NotNull(message = "El número de cuotas es obligatorio")
    @Positive(message = "El número de cuotas debe ser mayor que cero")
    private Integer numeroCuotas;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}