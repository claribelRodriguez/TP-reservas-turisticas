package com.app.TPreservasturisticas.repository;

import com.app.TPreservasturisticas.entity.Guia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuiaRepository extends JpaRepository<Guia, Long> {
    List<Guia> findByDisponibleTrue();
}
