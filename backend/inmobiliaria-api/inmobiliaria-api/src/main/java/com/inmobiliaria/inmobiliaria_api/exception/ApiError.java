package com.inmobiliaria.inmobiliaria_api.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private LocalDateTime fecha;
    private Integer estado;
    private String error;
    private String mensaje;
    private String ruta;
}
