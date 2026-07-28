package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.mapper.ReservaMapper;
import com.inmobiliaria.inmobiliaria_api.dto.request.ReservaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.ReservaResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Cliente;
import com.inmobiliaria.inmobiliaria_api.entity.Propiedad;
import com.inmobiliaria.inmobiliaria_api.entity.Reserva;
import com.inmobiliaria.inmobiliaria_api.exception.BusinessException;
import com.inmobiliaria.inmobiliaria_api.repository.ClienteRepository;
import com.inmobiliaria.inmobiliaria_api.repository.PropiedadRepository;
import com.inmobiliaria.inmobiliaria_api.repository.ReservaRepository;
import com.inmobiliaria.inmobiliaria_api.service.ReservaService;
import com.inmobiliaria.inmobiliaria_api.entity.Usuario;
import com.inmobiliaria.inmobiliaria_api.exception.ResourceNotFoundException;
import com.inmobiliaria.inmobiliaria_api.repository.UsuarioRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ReservaResponse guardar(ReservaRequest request) {

        Cliente cliente = clienteRepository
                .findById(request.getIdCliente())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cliente no encontrado"
                        )
                );

        Propiedad propiedad = propiedadRepository
                .findById(request.getIdPropiedad())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Propiedad no encontrada"
                        )
                );

        if (!Boolean.TRUE.equals(propiedad.getActivo())) {
            throw new BusinessException(
                    "La propiedad se encuentra inactiva"
            );
        }

        if (!"DISPONIBLE".equalsIgnoreCase(
                propiedad.getEstado()
        )) {
            throw new BusinessException(
                    "La propiedad no está disponible para reservar"
            );
        }

        Reserva reserva = new Reserva();

        reserva.setCliente(cliente);
        reserva.setPropiedad(propiedad);
        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setEstado("ACTIVA");
        reserva.setActivo(true);

        propiedad.setEstado("RESERVADA");

        propiedadRepository.save(propiedad);

        Reserva reservaGuardada =
                reservaRepository.save(reserva);

        return reservaMapper.toResponse(reservaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ReservaResponse> listar(
            Pageable pageable) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Page<Reserva> pagina;

        if ("CLIENTE".equalsIgnoreCase(
                usuario.getRol().getNombre()
        )) {

            Cliente cliente =
                    obtenerClienteAutenticado(usuario);

            pagina = reservaRepository
                    .findByClienteIdClienteAndActivoTrue(
                            cliente.getIdCliente(),
                            pageable
                    );

        } else {

            pagina = reservaRepository
                    .findByActivoTrue(pageable);
        }

        Page<ReservaResponse> paginaResponse =
                pagina.map(reservaMapper::toResponse);

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
    public ReservaResponse buscarPorId(Long id) {

        Reserva reserva = reservaRepository
                .findByIdReservaAndActivoTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reserva no encontrada"
                        )
                );

        Usuario usuario = obtenerUsuarioAutenticado();

        if ("CLIENTE".equalsIgnoreCase(
                usuario.getRol().getNombre()
        )) {

            Cliente cliente =
                    obtenerClienteAutenticado(usuario);

            if (!reserva.getCliente()
                    .getIdCliente()
                    .equals(cliente.getIdCliente())) {

                throw new ResourceNotFoundException(
                        "Reserva no encontrada"
                );
            }
        }

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
                        usuario.getPersona().getIdPersona()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cliente asociado al usuario no encontrado"
                        )
                );
    }

}