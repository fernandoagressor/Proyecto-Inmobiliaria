package com.inmobiliaria.inmobiliaria_api.repository;

import com.inmobiliaria.inmobiliaria_api.entity.Contrato;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    Long countByEstadoIgnoreCase(String estado);
    Page<Contrato> findByActivoTrue(Pageable pageable);

    Page<Contrato> findByClienteIdClienteAndActivoTrue(
            Long idCliente,
            Pageable pageable
    );
    Long countByClienteIdClienteAndActivoTrue(
            Long idCliente
    );

    Long countByClienteIdClienteAndEstadoIgnoreCaseAndActivoTrue(
            Long idCliente,
            String estado
    );

    @Query("""
    SELECT COUNT(DISTINCT c.propiedad.idPropiedad)
    FROM Contrato c
    WHERE c.cliente.idCliente = :idCliente
      AND c.activo = true
    """)
    Long contarPropiedadesPorCliente(
            @Param("idCliente") Long idCliente
    );

}
