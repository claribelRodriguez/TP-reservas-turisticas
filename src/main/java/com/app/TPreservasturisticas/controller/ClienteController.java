package com.app.TPreservasturisticas.controller;

import com.app.TPreservasturisticas.entity.Cliente;
import com.app.TPreservasturisticas.service.ClienteService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> agregar(Cliente cliente) {
        if(cliente == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(validarDatosCliente(cliente)) {
            Cliente nuevoCliente = clienteService.agregar(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    @GetMapping()
    public ResponseEntity<List<Cliente>> obtenerTodos() {
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> modificar(@PathVariable Long id,
                                             @RequestBody Cliente cliente) {
        if(cliente == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(validarDatosCliente(cliente)) {
            return ResponseEntity.ok(clienteService.modificar(id, cliente));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    private boolean validarDatosCliente(Cliente cliente) {
        //si es true los campos son correctos
        return !cliente.getNombre().isEmpty() && !cliente.getCuit().isEmpty();
    }
}
