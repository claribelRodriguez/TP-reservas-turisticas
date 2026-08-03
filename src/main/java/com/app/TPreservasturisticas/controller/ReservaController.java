package com.app.TPreservasturisticas.controller;

import com.app.TPreservasturisticas.dto.AgregarPagoDTO;
import com.app.TPreservasturisticas.dto.AgregarReservaDTO;
import com.app.TPreservasturisticas.entity.Reserva;
import com.app.TPreservasturisticas.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<Reserva> agregar(@RequestBody AgregarReservaDTO reserva) {
        Reserva creada = reservaService.agregar(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerTodas() {
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/pagos/{diasPlazo}")
    public ResponseEntity<Reserva> pagar(@PathVariable Long id, @RequestBody AgregarPagoDTO pagoDto, @PathVariable int diasPlazo) {
        Reserva actualizada = reservaService.registrarPago(id, pagoDto, diasPlazo);
        return ResponseEntity.ok(actualizada);
    }

    @PostMapping("/vencer-ahora/{diasPlazo}")
    public ResponseEntity<String> vencerAhora(@PathVariable int diasPlazo) {
        reservaService.vencerReservasNoPagadas(diasPlazo);
        return ResponseEntity.ok("listo, se vencieron las reservas que correspondian");
    }
}
