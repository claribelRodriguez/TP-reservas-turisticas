package com.app.TPreservasturisticas.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Experiencia_turistica")
@Setter
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ExperienciaTuristica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.PROTECTED)
    private TipoExperiencia tipoExperiencia;

    public ExperienciaTuristica(String nombre) {
        this.nombre = nombre;
    }

    public abstract Integer getCapacidadMaxima();
    public abstract Boolean getRequiereGuia();
    public abstract LocalDate getFechaInicio();
    public abstract Double getPrecioBase();
    public abstract Integer getUmbralDescuento();
    public abstract Double getPorcentajeDescuento();
    public abstract LocalDate getFechaFin();
}
