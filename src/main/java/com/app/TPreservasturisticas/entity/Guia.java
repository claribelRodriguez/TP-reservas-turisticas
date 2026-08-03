package com.app.TPreservasturisticas.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Guia")
@Getter
@Setter
@NoArgsConstructor
public class Guia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Boolean disponible = true;

    public Guia(String nombre) {
        this.nombre = nombre;
    }
}
