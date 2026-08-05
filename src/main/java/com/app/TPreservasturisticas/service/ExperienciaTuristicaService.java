package com.app.TPreservasturisticas.service;

import com.app.TPreservasturisticas.dto.ExperienciaDisponibilidadDTO;
import com.app.TPreservasturisticas.entity.ExperienciaTuristica;
import com.app.TPreservasturisticas.exception.NoEncontradoException;
import com.app.TPreservasturisticas.repository.ExperienciaTuristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienciaTuristicaService {

    @Autowired
    private ExperienciaTuristicaRepository experienciaTuristicaRepository;
    @Autowired
    private ReservaService reservaService;

    public ExperienciaTuristica obtenerPorId(Long id) {
        return experienciaTuristicaRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("no existe la experiencia"));
    }

    public List<ExperienciaTuristica> obtenerTodos() {
        return experienciaTuristicaRepository.findAll();
    }

    public ExperienciaDisponibilidadDTO obtenerDisponibilidadExperiencia(Long id) {
        ExperienciaTuristica experiencia = experienciaTuristicaRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("no existe la experiencia"));
        Integer ocupada = reservaService.getCapacidadOcupada(id);

        Boolean disponible = ocupada >= experiencia.getCapacidadMaxima();

        return new ExperienciaDisponibilidadDTO(
                experiencia.getNombre(),
                experiencia.getFechaInicio(),
                experiencia.getFechaFin(),
                experiencia.getCapacidadMaxima(),
                ocupada,
                disponible
        );
    }
}
