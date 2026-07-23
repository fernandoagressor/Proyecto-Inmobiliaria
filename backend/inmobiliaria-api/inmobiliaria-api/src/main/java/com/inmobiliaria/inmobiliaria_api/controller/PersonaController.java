package com.inmobiliaria.inmobiliaria_api.controller;

import com.inmobiliaria.inmobiliaria_api.dto.request.PersonaRequest;
import com.inmobiliaria.inmobiliaria_api.dto.response.PersonaResponse;
import com.inmobiliaria.inmobiliaria_api.entity.Persona;
import com.inmobiliaria.inmobiliaria_api.service.PersonaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {
    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }
    @PostMapping
    public ResponseEntity<PersonaResponse> guardar (
            @Valid @RequestBody PersonaRequest request){
        PersonaResponse response = personaService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<PersonaResponse>> listar() {

        return ResponseEntity.ok(personaService.listar());

    }
    @GetMapping("/{id}")
    public ResponseEntity<PersonaResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(personaService.buscarPorId(id));

    }
    @PutMapping("/{id}")
    public ResponseEntity<PersonaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PersonaRequest request) {

        return ResponseEntity.ok(personaService.actualizar(id, request));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        personaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

}
