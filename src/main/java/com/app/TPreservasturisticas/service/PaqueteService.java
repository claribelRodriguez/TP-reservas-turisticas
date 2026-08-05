package com.app.TPreservasturisticas.service;

import com.app.TPreservasturisticas.entity.Actividad;
import com.app.TPreservasturisticas.entity.Paquete;
import com.app.TPreservasturisticas.exception.NoEncontradoException;
import com.app.TPreservasturisticas.exception.ReglaNegocioException;
import com.app.TPreservasturisticas.repository.ActividadRepository;
import com.app.TPreservasturisticas.repository.PaqueteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaqueteService  {
    @Autowired
    private PaqueteRepository paqueteRepository;
    @Autowired
    private ActividadRepository actividadRepository;

    //POST
    public Paquete crear(Paquete paquete) {
        return paqueteRepository.save(paquete);
    }

    public Paquete agregarActividad(Long paqueteId, Long actividadId) {
        Paquete paqueteBD = paqueteRepository.findById(paqueteId)
                .orElseThrow(() -> new NoEncontradoException("el paquete no se encuentra"));

        Actividad actividadBD = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new NoEncontradoException("el paquete no se encuentra"));

        Long actividadPaquete = paqueteRepository.obtenerActividadPaquete(paqueteId, actividadId);

        if(actividadPaquete != null) {
            throw new ReglaNegocioException("La actividad ya se encuentra registrada");
        }

        paqueteBD.agregarActividad(actividadBD);

        return paqueteRepository.save(paqueteBD);
    }

    //PUT
    public Paquete obtenerPorId(Long id) {
        return paqueteRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("el paquete no se encuentra"));
    }

    public List<Paquete> obtenerTodos() {
        return paqueteRepository.findAll();
    }
}
