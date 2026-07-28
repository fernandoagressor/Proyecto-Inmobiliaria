package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.mapper.PropiedadMapper;
import com.inmobiliaria.inmobiliaria_api.dto.request.PropiedadRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PropiedadResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Propiedad;
import com.inmobiliaria.inmobiliaria_api.exception.ResourceAlreadyExistsException;
import com.inmobiliaria.inmobiliaria_api.exception.ResourceNotFoundException;
import com.inmobiliaria.inmobiliaria_api.repository.PropiedadRepository;
import com.inmobiliaria.inmobiliaria_api.service.PropiedadService;
import com.inmobiliaria.inmobiliaria_api.entity.Cliente;
import com.inmobiliaria.inmobiliaria_api.entity.Usuario;
import com.inmobiliaria.inmobiliaria_api.repository.ClienteRepository;
import com.inmobiliaria.inmobiliaria_api.repository.UsuarioRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PropiedadServiceImpl implements PropiedadService {

    private final PropiedadRepository propiedadRepository;
    private final PropiedadMapper propiedadMapper;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public PropiedadResponse guardar(PropiedadRequest request) {

        if (propiedadRepository.existsByCodigo(request.getCodigo())) {
            throw new ResourceAlreadyExistsException(
                    "Ya existe una propiedad registrada con el código "
                            + request.getCodigo()
            );
        }

        Propiedad propiedad = propiedadMapper.toEntity(request);

        propiedad.setActivo(true);

        Propiedad propiedadGuardada =
                propiedadRepository.save(propiedad);

        return propiedadMapper.toResponse(propiedadGuardada);
    }


    @Override
    @Transactional(readOnly = true)
    public PageResponse<PropiedadResponse> listar(
            Pageable pageable) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        Page<Propiedad> pagina;

        if ("CLIENTE".equalsIgnoreCase(
                usuario.getRol().getNombre()
        )) {

            Cliente cliente =
                    obtenerClienteAutenticado(usuario);

            pagina = propiedadRepository
                    .buscarPropiedadesPorCliente(
                            cliente.getIdCliente(),
                            pageable
                    );

        } else {

            pagina = propiedadRepository
                    .findByActivoTrue(pageable);
        }

        Page<PropiedadResponse> paginaResponse =
                pagina.map(propiedadMapper::toResponse);

        return new PageResponse<>(
                paginaResponse.getContent(),
                paginaResponse.getNumber(),
                paginaResponse.getSize(),
                paginaResponse.getTotalElements(),
                paginaResponse.getTotalPages(),
                paginaResponse.isFirst(),
                paginaResponse.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PropiedadResponse buscarPorId(Long id) {

        Propiedad propiedad = propiedadRepository
                .findByIdPropiedadAndActivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Propiedad no encontrada"
                        )
                );

        return propiedadMapper.toResponse(propiedad);
    }

    @Override
    @Transactional
    public PropiedadResponse actualizar(
            Long id,
            PropiedadRequest request
    ) {

        Propiedad propiedad = propiedadRepository
                .findByIdPropiedadAndActivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Propiedad no encontrada"
                        )
                );

        boolean codigoDuplicado =
                propiedadRepository
                        .existsByCodigoAndIdPropiedadNot(
                                request.getCodigo(),
                                id
                        );

        if (codigoDuplicado) {
            throw new ResourceAlreadyExistsException(
                    "Ya existe una propiedad registrada con el código "
                            + request.getCodigo()
            );
        }

        propiedad.setCodigo(request.getCodigo());
        propiedad.setTitulo(request.getTitulo());
        propiedad.setDescripcion(request.getDescripcion());
        propiedad.setDireccion(request.getDireccion());
        propiedad.setValor(request.getValor());
        propiedad.setEstado(request.getEstado());

        Propiedad propiedadActualizada =
                propiedadRepository.save(propiedad);

        return propiedadMapper.toResponse(propiedadActualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Propiedad propiedad = propiedadRepository
                .findByIdPropiedadAndActivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Propiedad no encontrada"
                        )
                );

        propiedad.setActivo(false);

        propiedadRepository.save(propiedad);
    }
    private Usuario obtenerUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new ResourceNotFoundException(
                    "Usuario no autenticado"
            );
        }

        String correo =
                authentication.getName();

        return usuarioRepository
                .findByCorreo(correo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario autenticado no encontrado"
                        )
                );
    }
    private Cliente obtenerClienteAutenticado(
            Usuario usuario) {

        return clienteRepository
                .findByPersonaIdPersonaAndActivoTrue(
                        usuario.getPersona()
                                .getIdPersona()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cliente asociado al usuario no encontrado"
                        )
                );
    }

}