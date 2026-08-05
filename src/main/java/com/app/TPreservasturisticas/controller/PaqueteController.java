package com.app.TPreservasturisticas.controller;

import com.app.TPreservasturisticas.entity.Paquete;
import com.app.TPreservasturisticas.service.PaqueteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paquetes")
public class PaqueteController {

    @Autowired
    private PaqueteService paqueteService;

    //POST
    /*@PostMapping
    public ResponseEntity<Paquete> agregar(@RequestBody Paquete paquete) {

    }*/
}
