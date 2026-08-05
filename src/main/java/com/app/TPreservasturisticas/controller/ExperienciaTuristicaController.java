package com.app.TPreservasturisticas.controller;

import com.app.TPreservasturisticas.dto.ExperienciaDisponibilidadDTO;
import com.app.TPreservasturisticas.entity.ExperienciaTuristica;
import com.app.TPreservasturisticas.service.ExperienciaTuristicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/experiencias")
public class ExperienciaTuristicaController {

    @Autowired
    private ExperienciaTuristicaService experienciaTuristicaService;

    //GET
    @GetMapping("/{id}")
    public ResponseEntity<ExperienciaTuristica> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(experienciaTuristicaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ExperienciaTuristica>> obtenerTodos() {
        return ResponseEntity.ok(experienciaTuristicaService.obtenerTodos());
    }

    @GetMapping("/{id}/disponibilidad")
    public ResponseEntity<ExperienciaDisponibilidadDTO> obtenerFechasDisponiblesDisponibilidad(@PathVariable Long id) {
        return ResponseEntity.ok(experienciaTuristicaService.obtenerDisponibilidadExperiencia(id));
    }
}
