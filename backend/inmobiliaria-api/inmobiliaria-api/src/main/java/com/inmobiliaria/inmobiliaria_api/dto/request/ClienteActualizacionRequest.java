package com.inmobiliaria.inmobiliaria_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteActualizacionRequest {

    @NotBlank(message = "El teléfono es obligatorio.")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres.")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Debe ingresar un correo electrónico válido.")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres.")
    private String correo;

    @NotBlank(message = "La dirección es obligatoria.")
    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres.")
    private String direccion;

    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;
}