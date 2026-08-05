package com.app.TPreservasturisticas.controller;

import com.app.TPreservasturisticas.entity.Actividad;
import com.app.TPreservasturisticas.service.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actividades")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @PostMapping
    public ResponseEntity<Actividad> agregar(@RequestBody Actividad actividad) {
        if(actividad == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(validacionDatosActividad(actividad)) {
             Actividad nuevaActividad = actividadService.agregar(actividad);
             return ResponseEntity.status(HttpStatus.CREATED).body(nuevaActividad);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actividad> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(actividadService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Actividad>> obtenerTodas() {
        return ResponseEntity.ok(actividadService.obtenerTodas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actividad> modificar(@PathVariable Long id,
                                               @RequestBody Actividad actividad) {
        if(actividad == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(validacionDatosActividad(actividad)) {
            return ResponseEntity.ok(actividadService.modificar(id, actividad));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
        actividadService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    //Extraigo las validaciones, más prolijo
    private Boolean validacionDatosActividad(Actividad actividad) {
        boolean nombreVacio = actividad.getNombre().isEmpty();
        boolean fechaInicioNull = actividad.getFechaInicio() == null;
        boolean fechaFinNull = actividad.getFechaFin() == null;
        boolean capacidadNull = actividad.getCapacidadMaxima() == null;
        boolean precioBaseNull = actividad.getPrecioBase() == null;
        boolean porcentajeCorrecto = actividad.getPorcentajeDescuento() >= 0 && actividad.getPorcentajeDescuento() <= 100;

        //si da true los campos estan bien
        return !nombreVacio && !fechaInicioNull && !capacidadNull && !precioBaseNull && porcentajeCorrecto;
    }
}
