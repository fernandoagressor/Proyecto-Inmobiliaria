package com.inmobiliaria.inmobiliaria_api.repository;

import com.inmobiliaria.inmobiliaria_api.entity.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository
        extends JpaRepository<Empleado, Long> {

    Page<Empleado> findByActivoTrue(Pageable pageable);

    Optional<Empleado> findByIdEmpleadoAndActivoTrue(
            Long idEmpleado
    );

    Optional<Empleado> findByPersonaIdPersonaAndActivoTrue(
            Long idPersona
    );

    boolean existsByPersonaIdPersonaAndActivoTrue(
            Long idPersona
    );
}