package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.mapper.EmpleadoMapper;
import com.inmobiliaria.inmobiliaria_api.dto.request.EmpleadoRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.EmpleadoResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Empleado;
import com.inmobiliaria.inmobiliaria_api.entity.Persona;
import com.inmobiliaria.inmobiliaria_api.entity.Rol;
import com.inmobiliaria.inmobiliaria_api.entity.Usuario;
import com.inmobiliaria.inmobiliaria_api.exception.BusinessException;
import com.inmobiliaria.inmobiliaria_api.exception.ResourceNotFoundException;
import com.inmobiliaria.inmobiliaria_api.repository.EmpleadoRepository;
import com.inmobiliaria.inmobiliaria_api.repository.PersonaRepository;
import com.inmobiliaria.inmobiliaria_api.repository.RolRepository;
import com.inmobiliaria.inmobiliaria_api.repository.UsuarioRepository;
import com.inmobiliaria.inmobiliaria_api.service.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpleadoMapper empleadoMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public EmpleadoResponse guardar(
            EmpleadoRequest request
    ) {

        validarDocumentoDisponible(
                request.getNumeroDocumento(),
                null
        );

        validarCorreoDisponible(
                request.getCorreo(),
                null
        );

        Persona persona = new Persona();

        persona.setTipoDocumento(
                request.getTipoDocumento()
        );

        persona.setNumeroDocumento(
                request.getNumeroDocumento()
        );

        persona.setNombres(
                request.getNombres()
        );

        persona.setApellidos(
                request.getApellidos()
        );

        persona.setTelefono(
                request.getTelefono()
        );

        persona.setCorreo(
                request.getCorreo()
        );

        persona.setActivo(true);

        Persona personaGuardada =
                personaRepository.save(persona);

        Rol rolEmpleado =
                rolRepository.findByNombre("EMPLEADO")
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "El rol EMPLEADO no existe"
                                )
                        );

        Empleado empleado = new Empleado();

        empleado.setPersona(personaGuardada);
        empleado.setCargo(request.getCargo());
        empleado.setFechaIngreso(
                request.getFechaIngreso()
        );
        empleado.setActivo(true);

        Empleado empleadoGuardado =
                empleadoRepository.save(empleado);

        Usuario usuario = new Usuario();

        usuario.setCorreo(
                request.getCorreo()
        );

        usuario.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        usuario.setActivo(true);
        usuario.setRol(rolEmpleado);
        usuario.setPersona(personaGuardada);

        usuarioRepository.save(usuario);

        return empleadoMapper.toResponse(
                empleadoGuardado
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<EmpleadoResponse> listar(
            Pageable pageable
    ) {

        Page<EmpleadoResponse> pagina =
                empleadoRepository
                        .findByActivoTrue(pageable)
                        .map(
                                empleadoMapper::toResponse
                        );

        return new PageResponse<>(
                pagina.getContent(),
                pagina.getNumber(),
                pagina.getSize(),
                pagina.getTotalElements(),
                pagina.getTotalPages(),
                pagina.isFirst(),
                pagina.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponse buscarPorId(
            Long idEmpleado
    ) {

        Empleado empleado =
                buscarEmpleadoActivo(idEmpleado);

        return empleadoMapper.toResponse(
                empleado
        );
    }

    @Override
    @Transactional
    public EmpleadoResponse actualizar(
            Long idEmpleado,
            EmpleadoRequest request
    ) {

        Empleado empleado =
                buscarEmpleadoActivo(idEmpleado);

        Persona persona =
                empleado.getPersona();

        String correoAnterior =
                persona.getCorreo();

        validarDocumentoDisponible(
                request.getNumeroDocumento(),
                persona.getIdPersona()
        );

        validarCorreoDisponible(
                request.getCorreo(),
                persona.getIdPersona()
        );

        persona.setTipoDocumento(
                request.getTipoDocumento()
        );

        persona.setNumeroDocumento(
                request.getNumeroDocumento()
        );

        persona.setNombres(
                request.getNombres()
        );

        persona.setApellidos(
                request.getApellidos()
        );

        persona.setTelefono(
                request.getTelefono()
        );

        persona.setCorreo(
                request.getCorreo()
        );

        personaRepository.save(persona);

        empleado.setCargo(
                request.getCargo()
        );

        empleado.setFechaIngreso(
                request.getFechaIngreso()
        );

        Empleado empleadoActualizado =
                empleadoRepository.save(empleado);

        Usuario usuario =
                usuarioRepository
                        .findByCorreo(correoAnterior)
                        .orElse(null);

        if (usuario != null) {

            usuario.setCorreo(
                    request.getCorreo()
            );

            if (
                    request.getPassword() != null &&
                            !request.getPassword().isBlank()
            ) {

                usuario.setPassword(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                );
            }

            usuarioRepository.save(usuario);
        }

        return empleadoMapper.toResponse(
                empleadoActualizado
        );
    }

    @Override
    @Transactional
    public void eliminar(
            Long idEmpleado
    ) {

        Empleado empleado =
                buscarEmpleadoActivo(idEmpleado);

        empleado.setActivo(false);

        Persona persona =
                empleado.getPersona();

        persona.setActivo(false);

        Usuario usuario =
                usuarioRepository
                        .findByCorreo(
                                persona.getCorreo()
                        )
                        .orElse(null);

        if (usuario != null) {
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        }

        personaRepository.save(persona);

        empleadoRepository.save(empleado);
    }

    private Empleado buscarEmpleadoActivo(
            Long idEmpleado
    ) {

        return empleadoRepository
                .findByIdEmpleadoAndActivoTrue(
                        idEmpleado
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Empleado no encontrado con ID: "
                                        + idEmpleado
                        )
                );
    }

    private void validarDocumentoDisponible(
            String numeroDocumento,
            Long idPersonaActual
    ) {

        personaRepository
                .findByNumeroDocumento(
                        numeroDocumento
                )
                .ifPresent(persona -> {

                    if (
                            idPersonaActual == null ||
                                    !persona
                                            .getIdPersona()
                                            .equals(
                                                    idPersonaActual
                                            )
                    ) {

                        throw new BusinessException(
                                "Ya existe una persona con el documento: "
                                        + numeroDocumento
                        );
                    }
                });
    }

    private void validarCorreoDisponible(
            String correo,
            Long idPersonaActual
    ) {

        personaRepository
                .findByCorreo(correo)
                .ifPresent(persona -> {

                    if (
                            idPersonaActual == null ||
                                    !persona
                                            .getIdPersona()
                                            .equals(
                                                    idPersonaActual
                                            )
                    ) {

                        throw new BusinessException(
                                "Ya existe una persona con el correo: "
                                        + correo
                        );
                    }
                });

        usuarioRepository
                .findByCorreo(correo)
                .ifPresent(usuario -> {

                    if (
                            idPersonaActual == null ||
                                    !usuario
                                            .getPersona()
                                            .getIdPersona()
                                            .equals(
                                                    idPersonaActual
                                            )
                    ) {

                        throw new BusinessException(
                                "Ya existe un usuario con el correo: "
                                        + correo
                        );
                    }
                });
    }
}