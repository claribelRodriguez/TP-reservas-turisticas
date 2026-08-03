package com.app.TPreservasturisticas.service;

import com.app.TPreservasturisticas.entity.Cliente;
import com.app.TPreservasturisticas.exception.NoEncontradoException;
import com.app.TPreservasturisticas.exception.ReglaNegocioException;
import com.app.TPreservasturisticas.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    //POST
    public Cliente agregar(Cliente cliente) {
        if(clienteRepository.existsByCuit(cliente.getCuit())) {
            throw new ReglaNegocioException("ya existe un cliente con este cuit");
        }

        return clienteRepository.save(cliente);
    }

    //GET
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("cliente no encontrado"));
    }

    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    //PUT
    public Cliente modificar(Long id, Cliente cliente) {
        Cliente clienteBD = clienteRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("cliente no encontrado"));

        clienteBD.setNombre(cliente.getNombre());
        return clienteRepository.save(clienteBD);
    }

    //DELETE
    public void eliminarPorId(Long id) {
        if(clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        }
    }
}
