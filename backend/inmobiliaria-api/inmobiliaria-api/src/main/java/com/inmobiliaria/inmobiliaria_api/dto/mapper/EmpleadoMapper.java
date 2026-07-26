package com.inmobiliaria.inmobiliaria_api.dto.mapper;

import com.inmobiliaria.inmobiliaria_api.dto.response.EmpleadoResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Empleado;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoMapper {

    public EmpleadoResponse toResponse(Empleado empleado) {

        return new EmpleadoResponse(
                empleado.getIdEmpleado(),
                empleado.getPersona().getIdPersona(),
                empleado.getPersona().getTipoDocumento(),
                empleado.getPersona().getNumeroDocumento(),
                empleado.getPersona().getNombres(),
                empleado.getPersona().getApellidos(),
                empleado.getPersona().getTelefono(),
                empleado.getPersona().getCorreo(),
                empleado.getCargo(),
                empleado.getFechaIngreso(),
                empleado.getActivo()
        );
    }
}