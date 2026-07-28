package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.mapper.PagoMapper;
import com.inmobiliaria.inmobiliaria_api.dto.request.PagoRequest;
import com.inmobiliaria.inmobiliaria_api.entity.Cliente;
import com.inmobiliaria.inmobiliaria_api.entity.Usuario;
import com.inmobiliaria.inmobiliaria_api.repository.ClienteRepository;
import com.inmobiliaria.inmobiliaria_api.repository.UsuarioRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PagoResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Contrato;
import com.inmobiliaria.inmobiliaria_api.entity.Pago;
import com.inmobiliaria.inmobiliaria_api.exception.BusinessException;
import com.inmobiliaria.inmobiliaria_api.exception.ResourceNotFoundException;
import com.inmobiliaria.inmobiliaria_api.repository.ContratoRepository;
import com.inmobiliaria.inmobiliaria_api.repository.PagoRepository;
import com.inmobiliaria.inmobiliaria_api.service.PagoService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final ContratoRepository contratoRepository;
    private final PagoMapper pagoMapper;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public PagoResponse guardar(PagoRequest request) {

        Contrato contrato = contratoRepository.findById(request.getIdContrato())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contrato no encontrado con ID: " + request.getIdContrato()
                ));

        if (!Boolean.TRUE.equals(contrato.getActivo())) {
            throw new BusinessException("El contrato se encuentra inactivo");
        }

        if ("PAGADO".equalsIgnoreCase(contrato.getEstado())) {
            throw new BusinessException("El contrato ya se encuentra pagado");
        }

        if (contrato.getSaldoPendiente() == null) {
            throw new BusinessException(
                    "El contrato no tiene un saldo pendiente configurado"
            );
        }

        if (request.getNumeroCuota() > contrato.getNumeroCuotas()) {
            throw new BusinessException(
                    "El número de cuota no puede ser mayor al número de cuotas del contrato"
            );
        }

        if (request.getValorPago()
                .compareTo(contrato.getSaldoPendiente()) > 0) {

            throw new BusinessException(
                    "El valor del pago no puede superar el saldo pendiente"
            );
        }

        BigDecimal nuevoSaldo = contrato.getSaldoPendiente()
                .subtract(request.getValorPago());

        contrato.setSaldoPendiente(nuevoSaldo);

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) == 0) {
            contrato.setEstado("PAGADO");
        }

        contratoRepository.save(contrato);

        Pago pago = pagoMapper.toEntity(request);

        pago.setContrato(contrato);
        pago.setActivo(true);

        Pago pagoGuardado = pagoRepository.save(pago);

        return pagoMapper.toResponse(pagoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PagoResponse> listar(Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String correo = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByCorreo(correo)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario autenticado no encontrado"
                        )
                );

        Page<Pago> pagina;

        if ("CLIENTE".equalsIgnoreCase(
                usuario.getRol().getNombre()
        )) {

            Cliente cliente = clienteRepository
                    .findByPersonaIdPersonaAndActivoTrue(
                            usuario.getPersona().getIdPersona()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Cliente asociado al usuario no encontrado"
                            )
                    );

            pagina = pagoRepository
                    .findByContratoClienteIdClienteAndActivoTrue(
                            cliente.getIdCliente(),
                            pageable
                    );

        } else {

            pagina = pagoRepository
                    .findByActivoTrue(pageable);
        }

        Page<PagoResponse> paginaResponse =
                pagina.map(pagoMapper::toResponse);

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
    public PagoResponse buscarPorId(Long idPago) {

        Pago pago = buscarPagoActivo(idPago);

        Usuario usuario = obtenerUsuarioAutenticado();

        if ("CLIENTE".equalsIgnoreCase(
                usuario.getRol().getNombre()
        )) {

            Cliente cliente = obtenerClienteAutenticado(usuario);

            Long idClientePago = pago
                    .getContrato()
                    .getCliente()
                    .getIdCliente();

            if (!idClientePago.equals(
                    cliente.getIdCliente()
            )) {

                throw new ResourceNotFoundException(
                        "Pago no encontrado"
                );
            }
        }

        return pagoMapper.toResponse(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorContrato(
            Long idContrato) {

        Contrato contrato = contratoRepository
                .findById(idContrato)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Contrato no encontrado con ID: "
                                        + idContrato
                        )
                );

        if (!Boolean.TRUE.equals(
                contrato.getActivo()
        )) {

            throw new BusinessException(
                    "El contrato se encuentra inactivo"
            );
        }

        Usuario usuario = obtenerUsuarioAutenticado();

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

        return pagoRepository
                .findByContratoIdContratoAndActivoTrue(
                        idContrato
                )
                .stream()
                .map(pagoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public PagoResponse anular(Long idPago) {

        Pago pago = buscarPagoActivo(idPago);

        Contrato contrato = pago.getContrato();

        BigDecimal saldoRestaurado = contrato.getSaldoPendiente()
                .add(pago.getValorPago());

        contrato.setSaldoPendiente(saldoRestaurado);

        if ("PAGADO".equalsIgnoreCase(contrato.getEstado())) {
            contrato.setEstado("VIGENTE");
        }

        pago.setActivo(false);

        contratoRepository.save(contrato);
        Pago pagoAnulado = pagoRepository.save(pago);

        return pagoMapper.toResponse(pagoAnulado);
    }

    private Pago buscarPagoActivo(Long idPago) {

        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pago no encontrado con ID: " + idPago
                ));

        if (!Boolean.TRUE.equals(pago.getActivo())) {
            throw new ResourceNotFoundException(
                    "Pago no encontrado o se encuentra anulado"
            );
        }

        return pago;
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