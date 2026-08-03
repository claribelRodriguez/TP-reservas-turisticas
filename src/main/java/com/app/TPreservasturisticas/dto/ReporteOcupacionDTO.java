package com.app.TPreservasturisticas.dto;

public record ReporteOcupacionDTO(
     String nombre,
     int capacidadMaxima,
     int ocupacion,
     int disponible
) {}
