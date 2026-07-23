package com.inmobiliaria.inmobiliaria_api.dto.mapper;

import com.inmobiliaria.inmobiliaria_api.dto.request.ReservaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.ReservaResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    public Reserva toEntity(ReservaRequest request) {

        Reserva reserva = new Reserva();

        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setEstado(request.getEstado());

        return reserva;
    }

    public ReservaResponse toResponse(Reserva reserva) {

        ReservaResponse response = new ReservaResponse();

        response.setIdReserva(reserva.getIdReserva());

        if (reserva.getCliente() != null) {
            response.setIdCliente(reserva.getCliente().getIdCliente());

            if (reserva.getCliente().getPersona() != null) {
                response.setNombreCliente(
                        reserva.getCliente().getPersona().getNombres() + " " +
                                reserva.getCliente().getPersona().getApellidos()
                );
            }
        }

        if (reserva.getPropiedad() != null) {
            response.setIdPropiedad(reserva.getPropiedad().getIdPropiedad());
            response.setCodigoPropiedad(reserva.getPropiedad().getCodigo());
        }

        response.setFechaReserva(reserva.getFechaReserva());
        response.setEstado(reserva.getEstado());
        response.setActivo(reserva.getActivo());

        return response;
    }

}