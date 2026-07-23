package com.inmobiliaria.inmobiliaria_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contratos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrato;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_propiedad", nullable = false)
    private Propiedad propiedad;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal cuotaInicial;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoPendiente;

    @Column(nullable = false)
    private Integer numeroCuotas;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Boolean activo;
}