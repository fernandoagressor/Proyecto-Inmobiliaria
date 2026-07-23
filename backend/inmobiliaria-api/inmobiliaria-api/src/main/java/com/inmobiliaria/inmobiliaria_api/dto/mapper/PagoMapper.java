package com.inmobiliaria.inmobiliaria_api.dto.mapper;

import com.inmobiliaria.inmobiliaria_api.dto.request.PagoRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PagoResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Pago;

import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    public Pago toEntity(PagoRequest request) {

        Pago pago = new Pago();

        pago.setNumeroCuota(request.getNumeroCuota());
        pago.setFechaPago(request.getFechaPago());
        pago.setValorPago(request.getValorPago());
        pago.setMedioPago(request.getMedioPago());
        pago.setObservacion(request.getObservacion());

        return pago;
    }

    public PagoResponse toResponse(Pago pago) {

        PagoResponse response = new PagoResponse();

        response.setIdPago(pago.getIdPago());

        response.setIdContrato(
                pago.getContrato().getIdContrato()
        );

        response.setNombreCliente(
                pago.getContrato()
                        .getCliente()
                        .getPersona()
                        .getNombres()
                        + " "
                        + pago.getContrato()
                        .getCliente()
                        .getPersona()
                        .getApellidos()
        );

        response.setCodigoPropiedad(
                pago.getContrato()
                        .getPropiedad()
                        .getCodigo()
        );

        response.setEstadoContrato(
                pago.getContrato()
                        .getEstado()
        );

        response.setNumeroCuota(pago.getNumeroCuota());
        response.setFechaPago(pago.getFechaPago());
        response.setValorPago(pago.getValorPago());
        response.setMedioPago(pago.getMedioPago());
        response.setObservacion(pago.getObservacion());

        response.setSaldoPendiente(
                pago.getContrato().getSaldoPendiente()
        );

        return response;
    }
}