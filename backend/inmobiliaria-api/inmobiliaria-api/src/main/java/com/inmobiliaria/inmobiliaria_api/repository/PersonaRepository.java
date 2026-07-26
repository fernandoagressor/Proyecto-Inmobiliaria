package com.inmobiliaria.inmobiliaria_api.repository;

import com.inmobiliaria.inmobiliaria_api.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    boolean existsByNumeroDocumento(String numeroDocumento);

    boolean existsByCorreo(String correo);

    boolean existsByCorreoAndIdPersonaNot(
            String correo,
            Long idPersona
    );

    Optional<Persona> findByNumeroDocumento(
            String numeroDocumento
    );

    Optional<Persona> findByCorreo(
            String correo
    );
}