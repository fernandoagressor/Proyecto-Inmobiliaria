package com.inmobiliaria.inmobiliaria_api.controller;

import com.inmobiliaria.inmobiliaria_api.dto.mapper.RolMapper;
import com.inmobiliaria.inmobiliaria_api.dto.request.RolRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.RolResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Rol;
import com.inmobiliaria.inmobiliaria_api.service.RolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    private final RolService rolService;
    private final RolMapper rolMapper;

    public RolController(RolService rolService, RolMapper rolMapper) {
        this.rolService = rolService;
        this.rolMapper = rolMapper;
    }
    @PostMapping
    public ResponseEntity<RolResponse> guardar(@Valid @RequestBody RolRequest request) {

        Rol rol = rolMapper.toEntity(request);

        Rol rolGuardado = rolService.guardar(rol);

        RolResponse response = rolMapper.toResponse(rolGuardado);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
    @GetMapping
    public ResponseEntity<List<RolResponse>> listar() {
        List<Rol> rols = rolService.listar();
        List<RolResponse> response = rolMapper.toResponseList(rols);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolResponse> buscarPorId(@PathVariable Long id) {
        Rol rol = rolService.buscarPorId(id);
        RolResponse response = rolMapper.toResponse(rol);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<RolResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RolRequest request) {
        Rol rol = rolMapper.toEntity(request);
        Rol rolActualizado = rolService.actualizar(id, rol);
        RolResponse response = rolMapper.toResponse(rolActualizado);

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        rolService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

}
