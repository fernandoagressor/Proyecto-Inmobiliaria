package com.inmobiliaria.inmobiliaria_api.service;

import com.inmobiliaria.inmobiliaria_api.dto.filter.ClienteFiltro;
import com.inmobiliaria.inmobiliaria_api.dto.request.ClienteActualizacionRequest;
import com.inmobiliaria.inmobiliaria_api.dto.request.ClienteRegistroRequest;
import com.inmobiliaria.inmobiliaria_api.dto.request.ClienteRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.ClienteResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClienteService {

    ClienteResponse guardar(ClienteRequest request);

    PageResponse<ClienteResponse> listar(
            ClienteFiltro filtro,
            Pageable pageable);

    ClienteResponse buscarPorId(Long id);

    ClienteResponse actualizar(Long id, ClienteActualizacionRequest request);

    ClienteResponse registrarCliente(ClienteRegistroRequest request);

    void eliminar(Long id);

}