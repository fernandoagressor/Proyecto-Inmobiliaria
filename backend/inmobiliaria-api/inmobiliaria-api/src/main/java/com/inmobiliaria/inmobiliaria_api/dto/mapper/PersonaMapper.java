package com.inmobiliaria.inmobiliaria_api.dto.mapper;

import com.inmobiliaria.inmobiliaria_api.dto.request.PersonaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PersonaResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Persona;
import org.springframework.stereotype.Component;

@Component
public class PersonaMapper {

    public Persona toEntity(PersonaRequest request) {

        Persona persona = new Persona();

        persona.setTipoDocumento(request.getTipoDocumento());
        persona.setNumeroDocumento(request.getNumeroDocumento());
        persona.setNombres(request.getNombres());
        persona.setApellidos(request.getApellidos());
        persona.setTelefono(request.getTelefono());
        persona.setCorreo(request.getCorreo());

        return persona;
    }

    public PersonaResponse toResponse(Persona persona) {

        return new PersonaResponse(
                persona.getIdPersona(),
                persona.getTipoDocumento(),
                persona.getNumeroDocumento(),
                persona.getNombres(),
                persona.getApellidos(),
                persona.getTelefono(),
                persona.getCorreo(),
                persona.getActivo()
        );
    }
}
