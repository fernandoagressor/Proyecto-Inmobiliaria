package com.inmobiliaria.inmobiliaria_api.dto.mapper;

import com.inmobiliaria.inmobiliaria_api.dto.request.FacturaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.FacturaResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Factura;
import org.springframework.stereotype.Component;

@Component
public class FacturaMapper {

    public Factura toEntity(FacturaRequest request) {

        Factura factura = new Factura();

        factura.setNumeroFactura(request.getNumeroFactura());
        factura.setFechaEmision(request.getFechaEmision());
        factura.setFechaVencimiento(request.getFechaVencimiento());
        factura.setValorFactura(request.getValorFactura());

        return factura;
    }

    public FacturaResponse toResponse(Factura factura) {

        FacturaResponse response = new FacturaResponse();

        response.setIdFactura(factura.getIdFactura());
        response.setIdContrato(factura.getContrato().getIdContrato());
        response.setNumeroFactura(factura.getNumeroFactura());
        response.setFechaEmision(factura.getFechaEmision());
        response.setFechaVencimiento(factura.getFechaVencimiento());
        response.setValorFactura(factura.getValorFactura());
        response.setEstado(factura.getEstado());

        return response;
    }
}