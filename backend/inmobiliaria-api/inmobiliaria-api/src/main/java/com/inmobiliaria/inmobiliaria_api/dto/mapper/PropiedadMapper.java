package com.inmobiliaria.inmobiliaria_api.dto.mapper;

import com.inmobiliaria.inmobiliaria_api.dto.request.PropiedadRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PropiedadResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Propiedad;
import org.springframework.stereotype.Component;

@Component
public class PropiedadMapper {

    public Propiedad toEntity(PropiedadRequest request) {

        Propiedad propiedad = new Propiedad();

        propiedad.setCodigo(request.getCodigo());
        propiedad.setTitulo(request.getTitulo());
        propiedad.setDescripcion(request.getDescripcion());
        propiedad.setDireccion(request.getDireccion());
        propiedad.setValor(request.getValor());
        propiedad.setEstado(request.getEstado());

        return propiedad;

    }

    public PropiedadResponse toResponse(Propiedad propiedad) {

        PropiedadResponse response = new PropiedadResponse();

        response.setIdPropiedad(propiedad.getIdPropiedad());
        response.setCodigo(propiedad.getCodigo());
        response.setTitulo(propiedad.getTitulo());
        response.setDescripcion(propiedad.getDescripcion());
        response.setDireccion(propiedad.getDireccion());
        response.setValor(propiedad.getValor());
        response.setEstado(propiedad.getEstado());
        response.setActivo(propiedad.getActivo());

        return response;

    }

}