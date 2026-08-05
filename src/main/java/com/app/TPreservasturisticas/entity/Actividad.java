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
public class Actividad extends ExperienciaTuristica {

    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;

    @Column(name = "requiere_guia", nullable = false)
    private Boolean requiereGuia;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "precio_base", nullable = false)
    private Double precioBase;

    @Column(name = "umbral_descuento", nullable = false)
    private Integer umbralDescuento;

    @Column(name = "porcentaje_descuento", nullable = false)
    private Double porcentajeDescuento;

    public Actividad(String nombre, LocalDate fechaInicio, Double precioBase, Integer umbralDescuento, Double porcentajeDescuento) {
        super(nombre);
        this.fechaInicio = fechaInicio;
        this.precioBase = precioBase;
        this.umbralDescuento = umbralDescuento;
        this.porcentajeDescuento = porcentajeDescuento;
        this.setTipoExperiencia(TipoExperiencia.ACTIVIDAD);
    }

}
