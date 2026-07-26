package com.inmobiliaria.inmobiliaria_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "empleado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpleado;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "id_persona",
            nullable = false,
            unique = true
    )
    private Persona persona;

    @Column(
            nullable = false,
            length = 100
    )
    private String cargo;

    @Column(nullable = false)
    private LocalDate fechaIngreso;

    @Column(nullable = false)
    private Boolean activo = true;
}