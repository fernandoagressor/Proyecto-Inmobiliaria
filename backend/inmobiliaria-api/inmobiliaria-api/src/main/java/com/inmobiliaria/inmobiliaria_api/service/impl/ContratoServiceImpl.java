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
    public PageResponse<ContratoResponse> listar(Pageable pageable) {

        Page<ContratoResponse> pagina = contratoRepository
                .findByActivoTrue(pageable)
                .map(contratoMapper::toResponse);

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
    public ContratoResponse buscarPorId(Long id) {

        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contrato no encontrado"));

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
}
