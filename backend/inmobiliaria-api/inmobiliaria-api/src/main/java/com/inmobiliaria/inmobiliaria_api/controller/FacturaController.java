package com.inmobiliaria.inmobiliaria_api.controller;

import com.inmobiliaria.inmobiliaria_api.dto.request.FacturaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.ClienteResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.FacturaResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.service.FacturaService;
import com.inmobiliaria.inmobiliaria_api.util.PageableUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping
    public ResponseEntity<FacturaResponse> guardar(
            @Valid @RequestBody FacturaRequest request) {

        return new ResponseEntity<>(
                facturaService.guardar(request),
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<PageResponse<FacturaResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idFactura") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                facturaService.listar(
                        PageableUtil.crear(
                                page,
                                size,
                                sortBy,
                                direction
                        )
                )
        );
    }

    @GetMapping("/{idFactura}")
    public ResponseEntity<FacturaResponse> buscarPorId(
            @PathVariable Long idFactura) {

        return ResponseEntity.ok(
                facturaService.buscarPorId(idFactura)
        );
    }

    @GetMapping("/contrato/{idContrato}")
    public ResponseEntity<List<FacturaResponse>> listarPorContrato(
            @PathVariable Long idContrato) {

        return ResponseEntity.ok(
                facturaService.listarPorContrato(idContrato)
        );
    }

    @PutMapping("/{idFactura}")
    public ResponseEntity<FacturaResponse> actualizar(
            @PathVariable Long idFactura,
            @Valid @RequestBody FacturaRequest request) {

        return ResponseEntity.ok(
                facturaService.actualizar(idFactura, request)
        );
    }

    @DeleteMapping("/{idFactura}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long idFactura) {

        facturaService.eliminar(idFactura);

        return ResponseEntity.noContent().build();
    }
}