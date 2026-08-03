package com.app.TPreservasturisticas.controller;

import com.app.TPreservasturisticas.entity.Guia;
import com.app.TPreservasturisticas.service.GuiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guias")
public class GuiaController {

    @Autowired
    private GuiaService guiaService;

    @PostMapping
    public ResponseEntity<Guia> agregar(Guia guia) {
        if(guia == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(!guia.getNombre().isEmpty()) {
            Guia nuevoGuia = guiaService.agregar(guia);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoGuia);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guia> obtenerPorId(Long id) {
        return ResponseEntity.ok(guiaService.obtenerPorId(id));
    }

    @GetMapping()
    public ResponseEntity<List<Guia>> obtenerTodos() {
        return ResponseEntity.ok(guiaService.obtenerTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Guia> modificar(Long id, Guia guia) {
        if(guia == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(guiaService.modificar(id, guia));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(Long id) {
        guiaService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
