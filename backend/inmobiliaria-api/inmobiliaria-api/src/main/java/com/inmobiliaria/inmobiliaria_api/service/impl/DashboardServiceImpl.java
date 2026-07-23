package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.response.DashboardResponse;
import com.inmobiliaria.inmobiliaria_api.repository.*;
import com.inmobiliaria.inmobiliaria_api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public DashboardResponse obtenerDashboard() {

        Long totalClientes = clienteRepository.count();
        Long totalPropiedades = propiedadRepository.count();
        Long totalContratos = contratoRepository.count();
        Long contratosVigentes = contratoRepository.countByEstadoIgnoreCase("VIGENTE");
        Long totalReservas = reservaRepository.count();
        Long facturasPendientes = facturaRepository.countByEstadoIgnoreCase("PENDIENTE");

        LocalDate fechaActual = LocalDate.now();

        LocalDate inicioMes = fechaActual.withDayOfMonth(1);

        LocalDate finMes = fechaActual.with(
                TemporalAdjusters.lastDayOfMonth()
        );

        BigDecimal pagosMes = pagoRepository.sumarPagosEntreFechas(
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

}