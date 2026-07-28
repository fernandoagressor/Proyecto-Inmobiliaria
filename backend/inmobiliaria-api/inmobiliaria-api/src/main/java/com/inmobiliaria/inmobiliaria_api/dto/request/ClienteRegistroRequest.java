package com.inmobiliaria.inmobiliaria_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRegistroRequest {

    @NotBlank(message = "El tipo de documento es obligatorio.")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio.")
    @Size(max = 20, message = "El número de documento no puede superar los 20 caracteres.")
    @Pattern(regexp = "^[0-9]+$", message = "El número de documento solo puede contener números.")
    private String numeroDocumento;

    @NotBlank(message = "Los nombres son obligatorios.")
    @Size(max = 100, message = "Los nombres no pueden superar los 100 caracteres.")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
            message = "Los nombres solo pueden contener letras."
    )
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios.")
    @Size(max = 100, message = "Los apellidos no pueden superar los 100 caracteres.")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
            message = "Los apellidos solo pueden contener letras."
    )
    private String apellidos;

    @NotBlank(message = "El teléfono es obligatorio.")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "El teléfono debe contener exactamente 10 dígitos."
    )
    private String telefono;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Debe ingresar un correo electrónico válido.")
    @Size(max = 150, message = "El correo no puede superar los 150 caracteres.")
    private String correo;

    @NotBlank(message = "La dirección es obligatoria.")
    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres.")
    private String direccion;

    @NotBlank(message = "La contraseña inicial es obligatoria.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;
}