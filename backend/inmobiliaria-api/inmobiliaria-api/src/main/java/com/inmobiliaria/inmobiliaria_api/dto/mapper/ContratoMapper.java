package com.inmobiliaria.inmobiliaria_api.dto.mapper;

import com.inmobiliaria.inmobiliaria_api.dto.request.ContratoRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.ContratoResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Contrato;
import org.springframework.stereotype.Component;

@Component
public class ContratoMapper {

    public Contrato toEntity(ContratoRequest request) {

        Contrato contrato = new Contrato();

        contrato.setValorTotal(request.getValorTotal());
        contrato.setCuotaInicial(request.getCuotaInicial());
        contrato.setNumeroCuotas(request.getNumeroCuotas());
        contrato.setFechaInicio(request.getFechaInicio());
        contrato.setEstado(request.getEstado());

        return contrato;
    }

    public ContratoResponse toResponse(Contrato contrato) {

        ContratoResponse response = new ContratoResponse();

        response.setIdContrato(contrato.getIdContrato());

        if (contrato.getCliente() != null) {
            response.setIdCliente(contrato.getCliente().getIdCliente());

            if (contrato.getCliente().getPersona() != null) {
                response.setNombreCliente(
                        contrato.getCliente().getPersona().getNombres() + " " +
                                contrato.getCliente().getPersona().getApellidos()
                );
            }
        }

        if (contrato.getPropiedad() != null) {
            response.setIdPropiedad(contrato.getPropiedad().getIdPropiedad());
            response.setCodigoPropiedad(contrato.getPropiedad().getCodigo());
        }

        response.setValorTotal(contrato.getValorTotal());
        response.setCuotaInicial(contrato.getCuotaInicial());
        response.setSaldoPendiente(contrato.getSaldoPendiente());
        response.setNumeroCuotas(contrato.getNumeroCuotas());
        response.setFechaInicio(contrato.getFechaInicio());
        response.setEstado(contrato.getEstado());

        return response;
    }
}