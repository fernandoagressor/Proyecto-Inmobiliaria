package com.inmobiliaria.inmobiliaria_api.service;

import com.inmobiliaria.inmobiliaria_api.dto.request.ContratoRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.ContratoResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;


public interface ContratoService {

    ContratoResponse guardar(ContratoRequest request);

    PageResponse<ContratoResponse> listar(Pageable pageable);

    ContratoResponse buscarPorId(Long id);

    ContratoResponse actualizar(Long id, ContratoRequest request);

    void eliminar(Long id);
}