package com.inmobiliaria.inmobiliaria_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    @ManyToOne
    @JoinColumn(name = "id_contrato", nullable = false)
    private Contrato contrato;

    @Column(nullable = false)
    private Integer numeroCuota;

    @Column(nullable = false)
    private LocalDate fechaPago;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorPago;

    @Column(nullable = false, length = 50)
    private String medioPago;

    @Column(length = 300)
    private String observacion;

    @Column(nullable = false)
    private Boolean activo;
}