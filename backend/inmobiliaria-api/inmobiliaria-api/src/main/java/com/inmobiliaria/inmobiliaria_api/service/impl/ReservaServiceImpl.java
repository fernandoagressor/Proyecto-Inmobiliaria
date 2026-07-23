package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.mapper.ReservaMapper;
import com.inmobiliaria.inmobiliaria_api.dto.request.ReservaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.ReservaResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Cliente;
import com.inmobiliaria.inmobiliaria_api.entity.Propiedad;
import com.inmobiliaria.inmobiliaria_api.entity.Reserva;
import com.inmobiliaria.inmobiliaria_api.repository.ClienteRepository;
import com.inmobiliaria.inmobiliaria_api.repository.PropiedadRepository;
import com.inmobiliaria.inmobiliaria_api.repository.ReservaRepository;
import com.inmobiliaria.inmobiliaria_api.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final PropiedadRepository propiedadRepository;
    private final ReservaMapper reservaMapper;

    @Override
    public ReservaResponse guardar(ReservaRequest request) {

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Propiedad propiedad = propiedadRepository.findById(request.getIdPropiedad())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        Reserva reserva = new Reserva();

        reserva.setCliente(cliente);
        reserva.setPropiedad(propiedad);
        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setEstado(request.getEstado());
        reserva.setActivo(true);

        return reservaMapper.toResponse(
                reservaRepository.save(reserva)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ReservaResponse> listar(Pageable pageable) {

        Page<ReservaResponse> pagina = reservaRepository
                .findByActivoTrue(pageable)
                .map(reservaMapper::toResponse);

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
    public ReservaResponse buscarPorId(Long id) {

        Reserva reserva = reservaRepository.findByIdReservaAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        return reservaMapper.toResponse(reserva);
    }

    @Override
    public ReservaResponse actualizar(Long id, ReservaRequest request) {

        Reserva reserva = reservaRepository.findByIdReservaAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Propiedad propiedad = propiedadRepository.findById(request.getIdPropiedad())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        reserva.setCliente(cliente);
        reserva.setPropiedad(propiedad);
        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setEstado(request.getEstado());

        return reservaMapper.toResponse(
                reservaRepository.save(reserva)
        );
    }

    @Override
    public void eliminar(Long id) {

        Reserva reserva = reservaRepository.findByIdReservaAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reserva.setActivo(false);

        reservaRepository.save(reserva);
    }

}