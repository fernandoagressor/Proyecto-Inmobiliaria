package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.mapper.ContratoMapper;
import com.inmobiliaria.inmobiliaria_api.dto.request.ContratoRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.ContratoResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.exception.BusinessException;
import com.inmobiliaria.inmobiliaria_api.entity.Cliente;
import com.inmobiliaria.inmobiliaria_api.entity.Contrato;
import com.inmobiliaria.inmobiliaria_api.entity.Propiedad;
import com.inmobiliaria.inmobiliaria_api.repository.ClienteRepository;
import com.inmobiliaria.inmobiliaria_api.repository.ContratoRepository;
import com.inmobiliaria.inmobiliaria_api.repository.PropiedadRepository;
import com.inmobiliaria.inmobiliaria_api.service.ContratoService;
import com.inmobiliaria.inmobiliaria_api.exception.ResourceNotFoundException;
import com.inmobiliaria.inmobiliaria_api.entity.Usuario;
import com.inmobiliaria.inmobiliaria_api.repository.UsuarioRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;
    private final ClienteRepository clienteRepository;
    private final PropiedadRepository propiedadRepository;
    private final ContratoMapper contratoMapper;
    private final UsuarioRepository usuarioRepository;

    @Override
    public ContratoResponse guardar(ContratoRequest request) {

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente no encontrado"));

        Propiedad propiedad = propiedadRepository.findById(request.getIdPropiedad())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Propiedad no encontrada"));

        if (request.getCuotaInicial().compareTo(request.getValorTotal()) > 0) {
            throw new BusinessException(
                    "La cuota inicial no puede ser mayor que el valor total");
        }

        Contrato contrato = contratoMapper.toEntity(request);

        BigDecimal saldoPendiente = request.getValorTotal()
                .subtract(request.getCuotaInicial());

        contrato.setSaldoPendiente(saldoPendiente);
        contrato.setCliente(cliente);
        contrato.setPropiedad(propiedad);
        contrato.setActivo(true);

        return contratoMapper.toResponse(
                contratoRepository.save(contrato)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ContratoResponse> listar(
            Pageable pageable) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Page<Contrato> pagina;

        if ("CLIENTE".equalsIgnoreCase(
                usuario.getRol().getNombre()
        )) {

            Cliente cliente =
                    obtenerClienteAutenticado(usuario);

            pagina = contratoRepository
                    .findByClienteIdClienteAndActivoTrue(
                            cliente.getIdCliente(),
                            pageable
                    );

        } else {

            pagina = contratoRepository
                    .findByActivoTrue(pageable);
        }

        Page<ContratoResponse> paginaResponse =
                pagina.map(contratoMapper::toResponse);

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
    public ContratoResponse buscarPorId(Long id) {

        Contrato contrato = contratoRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Contrato no encontrado"
                        )
                );

        if (!Boolean.TRUE.equals(contrato.getActivo())) {
            throw new ResourceNotFoundException(
                    "Contrato no encontrado"
            );
        }

        Usuario usuario =
                obtenerUsuarioAutenticado();

        if ("CLIENTE".equalsIgnoreCase(
                usuario.getRol().getNombre()
        )) {

            Cliente cliente =
                    obtenerClienteAutenticado(usuario);

            if (!contrato
                    .getCliente()
                    .getIdCliente()
                    .equals(cliente.getIdCliente())) {

                throw new ResourceNotFoundException(
                        "Contrato no encontrado"
                );
            }
        }

        return contratoMapper.toResponse(contrato);
    }

    @Override
    public ContratoResponse actualizar(Long id, ContratoRequest request) {

        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contrato no encontrado"));

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente no encontrado"));

        Propiedad propiedad = propiedadRepository.findById(request.getIdPropiedad())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Propiedad no encontrada"));

        if (request.getCuotaInicial().compareTo(request.getValorTotal()) > 0) {
            throw new BusinessException(
                    "La cuota inicial no puede ser mayor que el valor total");
        }

        contrato.setCliente(cliente);
        contrato.setPropiedad(propiedad);
        contrato.setValorTotal(request.getValorTotal());
        contrato.setCuotaInicial(request.getCuotaInicial());

        contrato.setSaldoPendiente(
                request.getValorTotal()
                        .subtract(request.getCuotaInicial())
        );
        contrato.setNumeroCuotas(request.getNumeroCuotas());
        contrato.setFechaInicio(request.getFechaInicio());
        contrato.setEstado(request.getEstado());

        return contratoMapper.toResponse(
                contratoRepository.save(contrato)
        );
    }

    @Override
    public void eliminar(Long id) {

        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contrato no encontrado"));

        contrato.setActivo(false);

        contratoRepository.save(contrato);
    }
    private Usuario obtenerUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new BusinessException(
                    "Usuario no autenticado"
            );
        }

        String correo = authentication.getName();

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
