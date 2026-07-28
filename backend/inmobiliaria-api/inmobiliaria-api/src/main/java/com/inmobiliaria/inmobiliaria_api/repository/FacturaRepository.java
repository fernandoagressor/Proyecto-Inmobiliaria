package com.inmobiliaria.inmobiliaria_api.repository;

import com.inmobiliaria.inmobiliaria_api.entity.Factura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository
        extends JpaRepository<Factura, Long> {

    Long countByEstadoIgnoreCase(String estado);

    Page<Factura> findByActivoTrue(
            Pageable pageable
    );

    Page<Factura> findByContratoClienteIdClienteAndActivoTrue(
            Long idCliente,
            Pageable pageable
    );

    Optional<Factura> findByIdFacturaAndActivoTrue(
            Long idFactura
    );

    List<Factura> findByContratoIdContratoAndActivoTrue(
            Long idContrato
    );

    boolean existsByNumeroFacturaAndActivoTrue(
            Integer numeroFactura
    );

    Long countByContratoClienteIdClienteAndEstadoIgnoreCaseAndActivoTrue(
            Long idCliente,
            String estado
    );
}