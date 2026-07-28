package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.mapper.FacturaMapper;
import com.inmobiliaria.inmobiliaria_api.dto.request.FacturaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.FacturaResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Contrato;
import com.inmobiliaria.inmobiliaria_api.entity.Factura;
import com.inmobiliaria.inmobiliaria_api.entity.Cliente;
import com.inmobiliaria.inmobiliaria_api.entity.Usuario;

import com.inmobiliaria.inmobiliaria_api.repository.ClienteRepository;
import com.inmobiliaria.inmobiliaria_api.repository.UsuarioRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.inmobiliaria.inmobiliaria_api.exception.BusinessException;
import com.inmobiliaria.inmobiliaria_api.exception.ResourceNotFoundException;
import com.inmobiliaria.inmobiliaria_api.repository.ContratoRepository;
import com.inmobiliaria.inmobiliaria_api.repository.FacturaRepository;
import com.inmobiliaria.inmobiliaria_api.service.FacturaService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final ContratoRepository contratoRepository;
    private final FacturaMapper facturaMapper;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public FacturaResponse guardar(FacturaRequest request) {

        Contrato contrato = buscarContratoActivo(request.getIdContrato());

        if (facturaRepository
                .existsByNumeroFacturaAndActivoTrue(request.getNumeroFactura())) {

            throw new BusinessException(
                    "Ya existe una factura activa con el número: "
                            + request.getNumeroFactura()
            );
        }

        validarFechas(request);
        validarValorFactura(request, contrato);

        Factura factura = facturaMapper.toEntity(request);

        factura.setContrato(contrato);
        factura.setEstado("PENDIENTE");
        factura.setActivo(true);

        Factura facturaGuardada = facturaRepository.save(factura);

        return facturaMapper.toResponse(facturaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FacturaResponse> listar(
            Pageable pageable) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        Page<Factura> pagina;

        if ("CLIENTE".equalsIgnoreCase(
                usuario.getRol().getNombre()
        )) {

            Cliente cliente =
                    obtenerClienteAutenticado(usuario);

            pagina = facturaRepository
                    .findByContratoClienteIdClienteAndActivoTrue(
                            cliente.getIdCliente(),
                            pageable
                    );

        } else {

            pagina = facturaRepository
                    .findByActivoTrue(pageable);
        }

        Page<FacturaResponse> paginaResponse =
                pagina.map(facturaMapper::toResponse);

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
    public FacturaResponse buscarPorId(Long idFactura) {

        Factura factura = buscarFacturaActiva(idFactura);

        Usuario usuario = obtenerUsuarioAutenticado();

        if ("CLIENTE".equalsIgnoreCase(
                usuario.getRol().getNombre()
        )) {

            Cliente cliente =
                    obtenerClienteAutenticado(usuario);

            Long idClienteFactura = factura
                    .getContrato()
                    .getCliente()
                    .getIdCliente();

            if (!idClienteFactura.equals(
                    cliente.getIdCliente()
            )) {

                throw new ResourceNotFoundException(
                        "Factura no encontrada"
                );
            }
        }

        return facturaMapper.toResponse(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponse> listarPorContrato(
            Long idContrato) {

        Contrato contrato =
                buscarContratoActivo(idContrato);

        Usuario usuario =
                obtenerUsuarioAutenticado();

        if ("CLIENTE".equalsIgnoreCase(
                usuario.getRol().getNombre()
        )) {

            Cliente cliente =
                    obtenerClienteAutenticado(usuario);

            Long idClienteContrato =
                    contrato.getCliente()
                            .getIdCliente();

            if (!idClienteContrato.equals(
                    cliente.getIdCliente()
            )) {

                throw new ResourceNotFoundException(
                        "Contrato no encontrado"
                );
            }
        }

        return facturaRepository
                .findByContratoIdContratoAndActivoTrue(
                        idContrato
                )
                .stream()
                .map(facturaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public FacturaResponse actualizar(
            Long idFactura,
            FacturaRequest request) {

        Factura factura = buscarFacturaActiva(idFactura);

        Contrato contrato = buscarContratoActivo(request.getIdContrato());

        boolean numeroFacturaCambio =
                !factura.getNumeroFactura()
                        .equals(request.getNumeroFactura());

        if (numeroFacturaCambio
                && facturaRepository.existsByNumeroFacturaAndActivoTrue(
                request.getNumeroFactura())) {

            throw new BusinessException(
                    "Ya existe una factura activa con el número: "
                            + request.getNumeroFactura()
            );
        }

        validarFechas(request);
        validarValorFactura(request, contrato);

        factura.setContrato(contrato);
        factura.setNumeroFactura(request.getNumeroFactura());
        factura.setFechaEmision(request.getFechaEmision());
        factura.setFechaVencimiento(request.getFechaVencimiento());
        factura.setValorFactura(request.getValorFactura());

        Factura facturaActualizada = facturaRepository.save(factura);

        return facturaMapper.toResponse(facturaActualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long idFactura) {

        Factura factura = buscarFacturaActiva(idFactura);

        if ("PAGADA".equalsIgnoreCase(factura.getEstado())) {
            throw new BusinessException(
                    "No se puede eliminar una factura pagada"
            );
        }

        factura.setActivo(false);

        facturaRepository.save(factura);
    }

    private Factura buscarFacturaActiva(Long idFactura) {

        return facturaRepository
                .findByIdFacturaAndActivoTrue(idFactura)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Factura no encontrada con ID: " + idFactura
                ));
    }

    private Contrato buscarContratoActivo(Long idContrato) {

        Contrato contrato = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contrato no encontrado con ID: " + idContrato
                ));

        if (!Boolean.TRUE.equals(contrato.getActivo())) {
            throw new BusinessException(
                    "El contrato se encuentra inactivo"
            );
        }

        return contrato;
    }

    private void validarFechas(FacturaRequest request) {

        if (request.getFechaVencimiento()
                .isBefore(request.getFechaEmision())) {

            throw new BusinessException(
                    "La fecha de vencimiento no puede ser anterior "
                            + "a la fecha de emisión"
            );
        }
    }

    private void validarValorFactura(
            FacturaRequest request,
            Contrato contrato) {

        if (contrato.getSaldoPendiente() == null) {
            throw new BusinessException(
                    "El contrato no tiene saldo pendiente configurado"
            );
        }

        if (request.getValorFactura()
                .compareTo(contrato.getSaldoPendiente()) > 0) {

            throw new BusinessException(
                    "El valor de la factura no puede superar "
                            + "el saldo pendiente del contrato"
            );
        }
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