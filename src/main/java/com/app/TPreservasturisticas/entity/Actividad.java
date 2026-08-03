package com.app.TPreservasturisticas.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Actividad")
@Getter
@Setter
@NoArgsConstructor
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;

    @Column(name = "requiere_guia", nullable = false)
    private Boolean requiereGuia;

    @Column(name = "precio_base", nullable = false)
    private Double precioBase;

    @Column(name = "umbral_descuento", nullable = false)
    private Integer umbralDescuento;

    @Column(name = "porcentaje_descuento", nullable = false)
    private Double porcentajeDescuento;

    public Actividad(String nombre, LocalDate fecha, Integer capacidadMaxima, Boolean requiereGuia, Double precioBase, Integer umbralDescuento, Double porcentajeDescuento) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.capacidadMaxima = capacidadMaxima;
        this.requiereGuia = requiereGuia;
        this.precioBase = precioBase;
        this.umbralDescuento = umbralDescuento;
        this.porcentajeDescuento = porcentajeDescuento;
    }

}
