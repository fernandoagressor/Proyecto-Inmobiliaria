package com.inmobiliaria.inmobiliaria_api.service;

import com.inmobiliaria.inmobiliaria_api.dto.request.PropiedadRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PropiedadResponse;
import org.springframework.data.domain.Pageable;

public interface PropiedadService {

    PropiedadResponse guardar(PropiedadRequest request);

    PageResponse<PropiedadResponse> listar(Pageable pageable);

    PropiedadResponse buscarPorId(Long id);

    PropiedadResponse actualizar(
            Long id,
            PropiedadRequest request
    );

    void eliminar(Long id);

}