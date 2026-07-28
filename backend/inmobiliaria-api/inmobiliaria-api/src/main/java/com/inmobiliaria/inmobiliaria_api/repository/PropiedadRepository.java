package com.inmobiliaria.inmobiliaria_api.repository;

import com.inmobiliaria.inmobiliaria_api.entity.Propiedad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    Optional<Propiedad> findByIdPropiedadAndActivoTrue(Long idPropiedad);

    Page<Propiedad> findByActivoTrue(Pageable pageable);

    @Query("""
        SELECT DISTINCT c.propiedad
        FROM Contrato c
        WHERE c.cliente.idCliente = :idCliente
          AND c.activo = true
          AND c.propiedad.activo = true
        """)
    Page<Propiedad> buscarPropiedadesPorCliente(
            @Param("idCliente") Long idCliente,
            Pageable pageable
    );

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdPropiedadNot(
            String codigo,
            Long idPropiedad
    );
}