package com.inmobiliaria.inmobiliaria_api.dto.mapper;


import com.inmobiliaria.inmobiliaria_api.dto.request.RolRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.RolResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Rol;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RolMapper {
    public Rol toEntity(RolRequest rolRequest) {
        Rol rol = new Rol();
        rol.setNombre(rolRequest.getNombre());
        return rol;
    }
    public RolResponse toResponse(Rol rol) {
        RolResponse response = new RolResponse();
        response.setIdRol(rol.getIdRol());
        response.setNombre(rol.getNombre());

        return response;
    }
    public List<RolResponse> toResponseList(List<Rol> rols) {
        List<RolResponse> responses = new ArrayList<>();

        for (Rol rol : rols) {
            responses.add(toResponse(rol));
        }
        return responses;
    }
}
