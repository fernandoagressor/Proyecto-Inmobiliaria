package com.inmobiliaria.inmobiliaria_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoResponse {

    private Long idEmpleado;

    private Long idPersona;

    private String tipoDocumento;

    private String numeroDocumento;

    private String nombres;

    private String apellidos;

    private String telefono;

    private String correo;

    private String cargo;

    private LocalDate fechaIngreso;

    private Boolean activo;
}