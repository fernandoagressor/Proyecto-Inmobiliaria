package com.inmobiliaria.inmobiliaria_api.service;

import com.inmobiliaria.inmobiliaria_api.dto.request.ReservaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.ReservaResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.UsuarioResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservaService {

    ReservaResponse guardar(ReservaRequest request);

    PageResponse<ReservaResponse> listar(Pageable pageable);

    ReservaResponse buscarPorId(Long id);

    ReservaResponse actualizar(Long id, ReservaRequest request);

    void eliminar(Long id);

}