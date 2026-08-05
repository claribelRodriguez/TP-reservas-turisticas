package com.app.TPreservasturisticas.dto;

import com.app.TPreservasturisticas.entity.MetodoPago;

public record AgregarPagoDTO (
        MetodoPago metodoPago,
        String cuitTitular,
        String nombreTitular
){}
