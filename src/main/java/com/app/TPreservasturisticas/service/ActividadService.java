package com.app.TPreservasturisticas.service;


import com.app.TPreservasturisticas.entity.Actividad;
import com.app.TPreservasturisticas.entity.EstadoReserva;
import com.app.TPreservasturisticas.exception.NoEncontradoException;
import com.app.TPreservasturisticas.repository.ActividadRepository;
import com.app.TPreservasturisticas.repository.ReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private ReservaRepository reservaRepository;


    //POST
    public Actividad agregar(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    //GET
    public Actividad obtenerPorId(Long id) {
        return actividadRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("no existe la actividad"));
    }

    public List<Actividad> obtenerTodas() {
        return actividadRepository.findAll();
    }

    //PUT
    @Transactional
    public Actividad modificar(Long id, Actividad actividad) {
        Actividad actividadBD = actividadRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("no existe la actividad"));

        actividadBD.setNombre(actividad.getNombre());
        actividadBD.setCapacidadMaxima(actividad.getCapacidadMaxima());
        actividadBD.setRequiereGuia(actividad.getRequiereGuia());
        actividadBD.setPrecioBase(actividad.getPrecioBase());
        actividadBD.setUmbralDescuento(actividad.getUmbralDescuento());
        actividadBD.setPorcentajeDescuento(actividad.getPorcentajeDescuento());

        return actividadRepository.save(actividadBD);
    }

    //DELETE
    @Transactional
    public void eliminarPorId(Long id) {
        if(actividadRepository.existsById(id)) {
            actividadRepository.deleteById(id);
        }
    }
}
