package com.inmobiliaria.inmobiliaria_api.controller;

import com.inmobiliaria.inmobiliaria_api.dto.response.DashboardResponse;
import com.inmobiliaria.inmobiliaria_api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> obtenerDashboard() {
        return ResponseEntity.ok(dashboardService.obtenerDashboard());
    }
}