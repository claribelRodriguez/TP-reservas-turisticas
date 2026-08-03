package com.app.TPreservasturisticas.service;

import com.app.TPreservasturisticas.entity.Guia;
import com.app.TPreservasturisticas.exception.NoEncontradoException;
import com.app.TPreservasturisticas.repository.GuiaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GuiaService {
    @Autowired
    private GuiaRepository guiaRepository;

    //POST
    public Guia agregar(Guia guia) {
        return guiaRepository.save(guia);
    }

    //GET
    public Guia obtenerPorId(Long id) {
        return guiaRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("guia no encontrado"));
    }

    public List<Guia> obtenerTodos() {
        return guiaRepository.findAll();
    }

    //PUT
    public Guia modificar(Long id, Guia guia) {
        Guia guiaBD = guiaRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("guia no encontrado"));

        guiaBD.setNombre(guia.getNombre());
        return guiaRepository.save(guiaBD);
    }

    //DELETE
    public void eliminarPorId(Long id) {
        if(guiaRepository.existsById(id)) {
            guiaRepository.deleteById(id);
        }
    }
}

