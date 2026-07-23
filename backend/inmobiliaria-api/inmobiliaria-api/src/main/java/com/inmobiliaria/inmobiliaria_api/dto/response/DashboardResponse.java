package com.inmobiliaria.inmobiliaria_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Long totalClientes;

    private Long totalPropiedades;

    private Long totalContratos;

    private Long contratosVigentes;

    private Long totalReservas;

    private Long facturasPendientes;

    private BigDecimal pagosMes;
}