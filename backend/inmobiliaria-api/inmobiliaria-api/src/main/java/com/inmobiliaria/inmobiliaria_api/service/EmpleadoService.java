package com.inmobiliaria.inmobiliaria_api.service;

import com.inmobiliaria.inmobiliaria_api.dto.request.EmpleadoRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.EmpleadoResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface EmpleadoService {

    EmpleadoResponse guardar(
            EmpleadoRequest request
    );

    PageResponse<EmpleadoResponse> listar(
            Pageable pageable
    );

    EmpleadoResponse buscarPorId(
            Long idEmpleado
    );

    EmpleadoResponse actualizar(
            Long idEmpleado,
            EmpleadoRequest request
    );

    void eliminar(
            Long idEmpleado
    );
}