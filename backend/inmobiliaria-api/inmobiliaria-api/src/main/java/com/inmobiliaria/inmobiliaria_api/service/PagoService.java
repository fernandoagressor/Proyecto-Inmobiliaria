package com.inmobiliaria.inmobiliaria_api.service;

import com.inmobiliaria.inmobiliaria_api.dto.request.PagoRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PagoResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PagoService {

    PagoResponse guardar(PagoRequest request);

    PageResponse<PagoResponse> listar(Pageable pageable);

    PagoResponse buscarPorId(Long idPago);

    List<PagoResponse> listarPorContrato(Long idContrato);

    PagoResponse anular(Long idPago);
}