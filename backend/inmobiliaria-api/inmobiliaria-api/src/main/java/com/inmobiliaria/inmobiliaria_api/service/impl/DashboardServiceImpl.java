package com.inmobiliaria.inmobiliaria_api.service.impl;

import com.inmobiliaria.inmobiliaria_api.dto.response.DashboardResponse;
import com.inmobiliaria.inmobiliaria_api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    @Override
    public DashboardResponse obtenerDashboard() {

        return new DashboardResponse(
                0L,
                0L,
                0L,
                0L,
                0L,
                0L,
                BigDecimal.ZERO
        );

    }

}