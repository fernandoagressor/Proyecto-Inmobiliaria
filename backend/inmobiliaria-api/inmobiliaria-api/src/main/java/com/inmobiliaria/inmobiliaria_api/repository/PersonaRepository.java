package com.inmobiliaria.inmobiliaria_api.repository;

import com.inmobiliaria.inmobiliaria_api.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonaRepository extends JpaRepository<Persona, Long> {

    boolean existsByNumeroDocumento(String numeroDocumento);

    boolean existsByCorreo(String correo);

    boolean existsByCorreoAndIdPersonaNot(String correo, Long idPersona);

}