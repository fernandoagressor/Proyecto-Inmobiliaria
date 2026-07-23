package com.inmobiliaria.inmobiliaria_api.repository;

import com.inmobiliaria.inmobiliaria_api.entity.Pago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Page<Pago> findByActivoTrue(Pageable pageable);

    List<Pago> findByContratoIdContratoAndActivoTrue(Long idContrato);
    @Query("""
       SELECT COALESCE(SUM(p.valorPago), 0)
       FROM Pago p
       WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin
       """)
    BigDecimal sumarPagosEntreFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );
}