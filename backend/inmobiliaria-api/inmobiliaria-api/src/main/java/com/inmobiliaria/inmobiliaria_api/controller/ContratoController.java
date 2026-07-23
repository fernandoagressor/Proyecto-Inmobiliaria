package com.inmobiliaria.inmobiliaria_api.controller;

import com.inmobiliaria.inmobiliaria_api.dto.request.ContratoRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.ContratoResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.service.ContratoService;
import com.inmobiliaria.inmobiliaria_api.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
public class ContratoController {

    private final ContratoService contratoService;

    @PostMapping
    public ResponseEntity<ContratoResponse> guardar(
            @Valid @RequestBody ContratoRequest request) {

        return new ResponseEntity<>(
                contratoService.guardar(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<ContratoResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idContrato") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                contratoService.listar(
                        PageableUtil.crear(
                                page,
                                size,
                                sortBy,
                                direction
                        )
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContratoRequest request) {

        return ResponseEntity.ok(
                contratoService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        contratoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}