package com.inmobiliaria.inmobiliaria_api.service;

import com.inmobiliaria.inmobiliaria_api.dto.request.UsuarioRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.UsuarioResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;

import org.springframework.data.domain.Pageable;


public interface UsuarioService {

    UsuarioResponse guardar(UsuarioRequest request);

    PageResponse<UsuarioResponse> listar(Pageable pageable);

    UsuarioResponse buscarPorId(Long id);

    UsuarioResponse actualizar(Long id, UsuarioRequest request);

    void eliminar(Long id);

}