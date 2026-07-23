package com.inmobiliaria.inmobiliaria_api.controller;

import com.inmobiliaria.inmobiliaria_api.dto.request.PagoRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PagoResponse;
import com.inmobiliaria.inmobiliaria_api.service.PagoService;
import com.inmobiliaria.inmobiliaria_api.util.PageableUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<PagoResponse> guardar(
            @Valid @RequestBody PagoRequest request) {

        return new ResponseEntity<>(
                pagoService.guardar(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<PagoResponse>> listar(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idPago") String sortBy,
            @RequestParam(defaultValue = "asc") String direction)
    {

        return ResponseEntity.ok(
                pagoService.listar(
                        PageableUtil.crear(
                                page,
                                size,
                                sortBy,
                                direction
                        )
                )
        );
    }

    @GetMapping("/{idPago}")
    public ResponseEntity<PagoResponse> buscarPorId(
            @PathVariable Long idPago) {

        return ResponseEntity.ok(
                pagoService.buscarPorId(idPago)
        );
    }

    @GetMapping("/contrato/{idContrato}")
    public ResponseEntity<List<PagoResponse>> listarPorContrato(
            @PathVariable Long idContrato) {

        return ResponseEntity.ok(
                pagoService.listarPorContrato(idContrato)
        );
    }

    @PatchMapping("/{idPago}/anular")
    public ResponseEntity<PagoResponse> anular(
            @PathVariable Long idPago) {

        return ResponseEntity.ok(
                pagoService.anular(idPago)
        );
    }
}