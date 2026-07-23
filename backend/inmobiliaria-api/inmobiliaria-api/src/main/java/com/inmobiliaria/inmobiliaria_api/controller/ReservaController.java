package com.inmobiliaria.inmobiliaria_api.controller;

import com.inmobiliaria.inmobiliaria_api.dto.request.ReservaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.ReservaResponse;
import com.inmobiliaria.inmobiliaria_api.service.ReservaService;
import com.inmobiliaria.inmobiliaria_api.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;


@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaResponse guardar(@RequestBody ReservaRequest request) {

        return reservaService.guardar(request);

    }

    @GetMapping
    public ResponseEntity<PageResponse<ReservaResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idReserva") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                reservaService.listar(
                        PageableUtil.crear(
                                page,
                                size,
                                sortBy,
                                direction
                        )
                )
        );
    }

    @GetMapping("/{id}")
    public ReservaResponse buscarPorId(@PathVariable Long id) {

        return reservaService.buscarPorId(id);

    }

    @PutMapping("/{id}")
    public ReservaResponse actualizar(@PathVariable Long id,
                                      @RequestBody ReservaRequest request) {

        return reservaService.actualizar(id, request);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {

        reservaService.eliminar(id);

    }

}