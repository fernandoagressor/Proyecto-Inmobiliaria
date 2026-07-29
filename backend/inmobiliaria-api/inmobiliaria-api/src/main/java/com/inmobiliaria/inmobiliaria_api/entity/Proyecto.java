package com.inmobiliaria.inmobiliaria_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proyecto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto")
    private Long idProyecto;

    @Column(name = "nombre", nullable = false, unique = true, length = 120)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "ciudad", nullable = false, length = 80)
    private String ciudad;

    @Column(name = "departamento", nullable = false, length = 80)
    private String departamento;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "max_lotes", nullable = false)
    private Integer maxLotes;

    @Column(name = "max_clientes", nullable = false)
    private Integer maxClientes;

    @Column(name = "max_empleados", nullable = false)
    private Integer maxEmpleados;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}