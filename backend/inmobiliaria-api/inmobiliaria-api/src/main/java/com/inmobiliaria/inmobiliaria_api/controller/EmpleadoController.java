package com.inmobiliaria.inmobiliaria_api.controller;

import com.inmobiliaria.inmobiliaria_api.dto.request.EmpleadoRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.EmpleadoResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.service.EmpleadoService;
import com.inmobiliaria.inmobiliaria_api.util.PageableUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @PostMapping
    public ResponseEntity<EmpleadoResponse> guardar(
            @Valid @RequestBody EmpleadoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        empleadoService.guardar(request)
                );
    }

    @GetMapping
    public ResponseEntity<PageResponse<EmpleadoResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idEmpleado") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                empleadoService.listar(
                        PageableUtil.crear(
                                page,
                                size,
                                sortBy,
                                direction
                        )
                )
        );
    }

    @GetMapping("/{idEmpleado}")
    public ResponseEntity<EmpleadoResponse> buscarPorId(
            @PathVariable Long idEmpleado) {

        return ResponseEntity.ok(
                empleadoService.buscarPorId(idEmpleado)
        );
    }

    @PutMapping("/{idEmpleado}")
    public ResponseEntity<EmpleadoResponse> actualizar(
            @PathVariable Long idEmpleado,
            @Valid @RequestBody EmpleadoRequest request) {

        return ResponseEntity.ok(
                empleadoService.actualizar(
                        idEmpleado,
                        request
                )
        );
    }

    @DeleteMapping("/{idEmpleado}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long idEmpleado) {

        empleadoService.eliminar(idEmpleado);

        return ResponseEntity
                .noContent()
                .build();
    }
}