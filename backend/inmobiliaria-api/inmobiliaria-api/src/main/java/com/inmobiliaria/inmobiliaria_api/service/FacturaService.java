package com.inmobiliaria.inmobiliaria_api.service;

import com.inmobiliaria.inmobiliaria_api.dto.request.FacturaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.FacturaResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FacturaService {

    FacturaResponse guardar(FacturaRequest request);

    PageResponse<FacturaResponse> listar(Pageable pageable);

    FacturaResponse buscarPorId(Long idFactura);

    List<FacturaResponse> listarPorContrato(Long idContrato);

    FacturaResponse actualizar(Long idFactura, FacturaRequest request);

    void eliminar(Long idFactura);
}