package com.app.TPreservasturisticas.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Paquete")
@Getter
@Setter
@NoArgsConstructor
public class Paquete extends ExperienciaTuristica {
    @ManyToMany
    @JoinTable(
            name = "paquete_experiencias",
            joinColumns = @JoinColumn(name = "paquete_id"),
            inverseJoinColumns = @JoinColumn(name = "experiencia_id")
    )
    @Setter(AccessLevel.NONE)
    public List<Actividad> actividades = new ArrayList<>();



    public Paquete(String nombre) {
        super(nombre);
        this.setTipoExperiencia(TipoExperiencia.PAQUETE);
    }

    @Override
    public Integer getCapacidadMaxima() {
        if(this.actividades.isEmpty()) return null;

        Integer capacidadMinima = null;
        for(Actividad actividad : actividades) {
            if(capacidadMinima == null || actividad.getCapacidadMaxima() < capacidadMinima) {
                capacidadMinima = actividad.getCapacidadMaxima();
            }
        }
        return capacidadMinima;
    }

    @Override
    public Boolean getRequiereGuia() {
        if(this.actividades.isEmpty()) return false;

        for(Actividad actividad : actividades) {
            if(actividad.getRequiereGuia()) return true;
        }

        return false;
    }

    @Override
    public LocalDate getFechaInicio() {
        if(this.actividades.isEmpty()) return null;

        LocalDate fechaMasPronta = null;
        for(Actividad actividad : actividades) {
            if(fechaMasPronta == null || actividad.getFechaInicio().isBefore(fechaMasPronta)) {
                fechaMasPronta = actividad.getFechaInicio();
            }
        }
        return fechaMasPronta;
    }

    @Override
    public Double getPrecioBase() {
        if(this.actividades.isEmpty()) return null;

        Double sumaPrecio = 0.0;
        for(Actividad actividad : actividades) {
            sumaPrecio += actividad.getPrecioBase();
        }
        return sumaPrecio;
    }

    @Override
    public Integer getUmbralDescuento() {
        if(this.actividades.isEmpty()) return null;

        Integer umbralMinimo = null;
        for(Actividad actividad : actividades) {
            if(umbralMinimo == null || actividad.getUmbralDescuento() < umbralMinimo) {
                umbralMinimo = actividad.getUmbralDescuento();
            }
        }
        return umbralMinimo;
    }

    @Override
    public Double getPorcentajeDescuento() {
        if(this.actividades.isEmpty()) return null;

        Double porcentajeMinimo = null;
        for(Actividad actividad : actividades) {
            if(porcentajeMinimo == null || actividad.getPorcentajeDescuento() < porcentajeMinimo) {
                porcentajeMinimo = actividad.getPorcentajeDescuento();
            }
        }
        return porcentajeMinimo;
    }

    @Override
    public LocalDate getFechaFin() {
        if(this.actividades.isEmpty()) return null;

        LocalDate fechaMasLejana = null;
        for(Actividad actividad : actividades) {
            if(fechaMasLejana == null || actividad.getFechaFin().isAfter(fechaMasLejana)) {
                fechaMasLejana = actividad.getFechaFin();
            }
        }
        return fechaMasLejana;
    }

    public void agregarActividad(Actividad actividad) {
        this.actividades.add(actividad);
    }
}
