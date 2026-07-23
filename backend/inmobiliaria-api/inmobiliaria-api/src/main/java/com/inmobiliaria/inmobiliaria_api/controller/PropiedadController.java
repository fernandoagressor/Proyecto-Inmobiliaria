package com.inmobiliaria.inmobiliaria_api.controller;

import com.inmobiliaria.inmobiliaria_api.dto.request.PropiedadRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PropiedadResponse;
import com.inmobiliaria.inmobiliaria_api.service.PropiedadService;
import com.inmobiliaria.inmobiliaria_api.util.PageableUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/propiedades")
@RequiredArgsConstructor
public class PropiedadController {

    private final PropiedadService propiedadService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PropiedadResponse guardar(
            @Valid @RequestBody PropiedadRequest request
    ) {
        return propiedadService.guardar(request);
    }

    @GetMapping
    public ResponseEntity<PageResponse<PropiedadResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idPropiedad") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        return ResponseEntity.ok(
                propiedadService.listar(
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
    public PropiedadResponse buscarPorId(
            @PathVariable Long id
    ) {
        return propiedadService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public PropiedadResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PropiedadRequest request
    ) {
        return propiedadService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable Long id
    ) {
        propiedadService.eliminar(id);
    }

}