package com.inmobiliaria.inmobiliaria_api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropiedadRequest {

    @NotBlank(message = "El código es obligatorio.")
    @Size(max = 30, message = "El código no puede superar los 30 caracteres.")
    private String codigo;

    @NotBlank(message = "El título es obligatorio.")
    @Size(max = 150, message = "El título no puede superar los 150 caracteres.")
    private String titulo;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres.")
    private String descripcion;

    @NotBlank(message = "La dirección es obligatoria.")
    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres.")
    private String direccion;

    @NotNull(message = "El valor es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "El valor debe ser mayor que cero.")
    private Double valor;

    @NotBlank(message = "El estado es obligatorio.")
    @Size(max = 30, message = "El estado no puede superar los 30 caracteres.")
    private String estado;

}