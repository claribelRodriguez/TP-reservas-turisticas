package com.app.TPreservasturisticas.controller;

import com.app.TPreservasturisticas.dto.ReporteOcupacionDTO;
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

    @GetMapping("/{id}/ocupacion")
    public ResponseEntity<ReporteOcupacionDTO> ocupacion(@PathVariable Long id) {
        Actividad actividad = actividadService.obtenerPorId(id);
        int ocupacion = actividadService.getCapacidadOcupada(id);

        ReporteOcupacionDTO reporte = new ReporteOcupacionDTO(
                actividad.getNombre(),
                actividad.getCapacidadMaxima(),
                ocupacion,
                actividad.getCapacidadMaxima() - ocupacion
        );

        return ResponseEntity.ok(reporte);
    }

    @PutMapping
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
        return ResponseEntity.notFound().build();
    }

    //Extraigo las validaciones, más prolijo
    private Boolean validacionDatosActividad(Actividad actividad) {
        boolean nombreVacio = actividad.getNombre().isEmpty();
        boolean fechaNull = actividad.getFecha() == null;
        boolean capacidadNull = actividad.getCapacidadMaxima() == null;
        boolean precioBaseNull = actividad.getPrecioBase() == null;
        boolean porcentajeCorrecto = actividad.getPorcentajeDescuento() >= 0 && actividad.getPorcentajeDescuento() <= 100;

        //si da true los campos estan bien
        return !nombreVacio && !fechaNull && !capacidadNull && !precioBaseNull && porcentajeCorrecto;
    }
}
