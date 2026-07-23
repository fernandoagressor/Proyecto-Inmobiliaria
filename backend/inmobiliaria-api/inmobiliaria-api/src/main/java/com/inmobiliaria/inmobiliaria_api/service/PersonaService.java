package com.inmobiliaria.inmobiliaria_api.service;

import com.inmobiliaria.inmobiliaria_api.dto.request.PersonaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PersonaResponse;

import java.util.List;

public interface PersonaService {

    PersonaResponse guardar(PersonaRequest request);

    List<PersonaResponse> listar();

    PersonaResponse buscarPorId(Long id);

    PersonaResponse actualizar(Long id, PersonaRequest request);

    void eliminar(Long id);

}