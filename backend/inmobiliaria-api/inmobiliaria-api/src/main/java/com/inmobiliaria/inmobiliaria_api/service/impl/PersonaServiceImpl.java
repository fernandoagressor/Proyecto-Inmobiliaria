package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.request.PersonaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PersonaResponse;
import com.inmobiliaria.inmobiliaria_api.dto.mapper.PersonaMapper;
import com.inmobiliaria.inmobiliaria_api.entity.Persona;
import com.inmobiliaria.inmobiliaria_api.repository.PersonaRepository;
import com.inmobiliaria.inmobiliaria_api.service.PersonaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final PersonaMapper personaMapper;

    public PersonaServiceImpl(PersonaRepository personaRepository,
                              PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    @Override
    @Transactional
    public PersonaResponse guardar(PersonaRequest request) {
        if (personaRepository.existsByNumeroDocumento(request.getNumeroDocumento())){
            throw new RuntimeException("Ya existe una persona con este documento.");
        }
        if (personaRepository.existsByCorreo(request.getCorreo())){
            throw new RuntimeException("Ya existe una persona con este correo.");
        }
        Persona persona = personaMapper.toEntity(request);

        Persona personaGuardada = personaRepository.save(persona);

        return personaMapper.toResponse(personaGuardada);
    }

    @Override
    public List<PersonaResponse> listar() {

        return personaRepository.findAll()
                .stream()
                .map(personaMapper::toResponse)
                .toList();

    }

    @Override
    public PersonaResponse buscarPorId(Long id) {

        Persona persona = personaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Persona no encontrada."));

        return personaMapper.toResponse(persona);
    }

    @Override
    @Transactional
    public PersonaResponse actualizar(Long id, PersonaRequest request) {

        Persona persona = personaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Persona no encontrada."));

        persona.setTipoDocumento(request.getTipoDocumento());
        persona.setNumeroDocumento(request.getNumeroDocumento());
        persona.setNombres(request.getNombres());
        persona.setApellidos(request.getApellidos());
        persona.setTelefono(request.getTelefono());
        persona.setCorreo(request.getCorreo());

        Persona personaActualizada = personaRepository.save(persona);

        return personaMapper.toResponse(personaActualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Persona persona = personaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Persona no encontrada."));

        persona.setActivo(false);

        personaRepository.save(persona);
    }
}