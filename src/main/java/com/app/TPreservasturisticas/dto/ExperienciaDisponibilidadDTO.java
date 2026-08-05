package com.app.TPreservasturisticas.dto;

import java.time.LocalDate;

public record ExperienciaDisponibilidadDTO(
        String nombre,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Integer capacidadMaxima,
        Integer capacidadOcupada,
        Boolean disponible
) {}
