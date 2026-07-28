package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.response.DashboardResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Cliente;
import com.inmobiliaria.inmobiliaria_api.entity.Usuario;
import com.inmobiliaria.inmobiliaria_api.exception.BusinessException;
import com.inmobiliaria.inmobiliaria_api.exception.ResourceNotFoundException;
import com.inmobiliaria.inmobiliaria_api.repository.*;
import com.inmobiliaria.inmobiliaria_api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ClienteRepository clienteRepository;
    private final PropiedadRepository propiedadRepository;
    private final ContratoRepository contratoRepository;
    private final ReservaRepository reservaRepository;
    private final FacturaRepository facturaRepository;
    private final PagoRepository pagoRepository;

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse obtenerDashboard() {

        Usuario usuario = obtenerUsuarioAutenticado();

        if ("CLIENTE".equalsIgnoreCase(
                usuario.getRol().getNombre()
        )) {

            return obtenerDashboardCliente(usuario);
        }

        return obtenerDashboardGlobal();
    }

    private DashboardResponse obtenerDashboardGlobal() {

        Long totalClientes =
                clienteRepository.count();

        Long totalPropiedades =
                propiedadRepository.count();

        Long totalContratos =
                contratoRepository.count();

        Long contratosVigentes =
                contratoRepository
                        .countByEstadoIgnoreCase(
                                "VIGENTE"
                        );

        Long totalReservas =
                reservaRepository.count();

        Long facturasPendientes =
                facturaRepository
                        .countByEstadoIgnoreCase(
                                "PENDIENTE"
                        );

        LocalDate fechaActual =
                LocalDate.now();

        LocalDate inicioMes =
                fechaActual.withDayOfMonth(1);

        LocalDate finMes =
                fechaActual.with(
                        TemporalAdjusters.lastDayOfMonth()
                );

        BigDecimal pagosMes =
                pagoRepository
                        .sumarPagosEntreFechas(
                                inicioMes,
                                finMes
                        );

        return new DashboardResponse(
                totalClientes,
                totalPropiedades,
                totalContratos,
                contratosVigentes,
                totalReservas,
                facturasPendientes,
                pagosMes
        );
    }

    private DashboardResponse obtenerDashboardCliente(
            Usuario usuario
    ) {

        Cliente cliente = clienteRepository
                .findByPersonaIdPersonaAndActivoTrue(
                        usuario.getPersona().getIdPersona()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cliente asociado al usuario no encontrado"
                        )
                );

        Long idCliente =
                cliente.getIdCliente();

        /*
         * Este valor ya no representa el total global de clientes.
         * Angular dejará de mostrar esta tarjeta para CLIENTE.
         */
        Long totalClientes = 1L;

        Long totalPropiedades =
                contratoRepository
                        .contarPropiedadesPorCliente(
                                idCliente
                        );

        Long totalContratos =
                contratoRepository
                        .countByClienteIdClienteAndActivoTrue(
                                idCliente
                        );

        Long contratosVigentes =
                contratoRepository
                        .countByClienteIdClienteAndEstadoIgnoreCaseAndActivoTrue(
                                idCliente,
                                "VIGENTE"
                        );

        Long totalReservas =
                reservaRepository
                        .countByClienteIdClienteAndActivoTrue(
                                idCliente
                        );

        Long facturasPendientes =
                facturaRepository
                        .countByContratoClienteIdClienteAndEstadoIgnoreCaseAndActivoTrue(
                                idCliente,
                                "PENDIENTE"
                        );

        LocalDate fechaActual =
                LocalDate.now();

        LocalDate inicioMes =
                fechaActual.withDayOfMonth(1);

        LocalDate finMes =
                fechaActual.with(
                        TemporalAdjusters.lastDayOfMonth()
                );

        BigDecimal pagosMes =
                pagoRepository
                        .sumarPagosClienteEntreFechas(
                                idCliente,
                                inicioMes,
                                finMes
                        );

        return new DashboardResponse(
                totalClientes,
                totalPropiedades,
                totalContratos,
                contratosVigentes,
                totalReservas,
                facturasPendientes,
                pagosMes
        );
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
}