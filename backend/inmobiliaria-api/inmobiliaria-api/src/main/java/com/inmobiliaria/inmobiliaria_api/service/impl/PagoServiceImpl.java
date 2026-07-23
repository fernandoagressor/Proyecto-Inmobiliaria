package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.mapper.PagoMapper;
import com.inmobiliaria.inmobiliaria_api.dto.request.PagoRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.ClienteResponse;
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

        Page<PagoResponse> pagina = pagoRepository
                .findByActivoTrue(pageable)
                .map(pagoMapper::toResponse);

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
    public PagoResponse buscarPorId(Long idPago) {

        Pago pago = buscarPagoActivo(idPago);

        return pagoMapper.toResponse(pago);
    }

    @Override
    public List<PagoResponse> listarPorContrato(Long idContrato) {

        Contrato contrato = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contrato no encontrado con ID: " + idContrato
                ));

        if (!Boolean.TRUE.equals(contrato.getActivo())) {
            throw new BusinessException("El contrato se encuentra inactivo");
        }

        return pagoRepository
                .findByContratoIdContratoAndActivoTrue(idContrato)
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
}