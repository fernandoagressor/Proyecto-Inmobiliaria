package com.inmobiliaria.inmobiliaria_api.repository;

import com.inmobiliaria.inmobiliaria_api.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>,
        JpaSpecificationExecutor<Cliente> {

    Optional<Cliente> findByIdClienteAndActivoTrue(Long idCliente);

    Page<Cliente> findByActivoTrue(Pageable pageable);

    Optional<Cliente> findByPersonaIdPersonaAndActivoTrue(
            Long idPersona
    );
}