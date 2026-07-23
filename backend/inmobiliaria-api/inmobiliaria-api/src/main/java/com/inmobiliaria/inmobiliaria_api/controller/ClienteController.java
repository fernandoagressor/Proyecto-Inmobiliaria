package com.inmobiliaria.inmobiliaria_api.controller;

import com.inmobiliaria.inmobiliaria_api.dto.filter.ClienteFiltro;
import com.inmobiliaria.inmobiliaria_api.dto.request.ClienteActualizacionRequest;
import com.inmobiliaria.inmobiliaria_api.dto.request.ClienteRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.ClienteResponse;
import com.inmobiliaria.inmobiliaria_api.dto.response.PageResponse;
import com.inmobiliaria.inmobiliaria_api.service.ClienteService;
import com.inmobiliaria.inmobiliaria_api.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import com.inmobiliaria.inmobiliaria_api.dto.request.ClienteRegistroRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponse> guardar(@RequestBody ClienteRequest request) {
        return new ResponseEntity<>(clienteService.guardar(request), HttpStatus.CREATED);
    }
    @PostMapping("/registro")
    public ResponseEntity<ClienteResponse> registrarCliente(
            @Valid @RequestBody ClienteRegistroRequest request) {

        return new ResponseEntity<>(
                clienteService.registrarCliente(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<ClienteResponse>> listar(
            @ParameterObject ClienteFiltro filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idCliente") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                clienteService.listar(
                        filtro,
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
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteActualizacionRequest request) {

        return ResponseEntity.ok(
                clienteService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}