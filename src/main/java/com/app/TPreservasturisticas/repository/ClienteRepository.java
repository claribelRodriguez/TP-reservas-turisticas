package com.app.TPreservasturisticas.repository;

import com.app.TPreservasturisticas.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCuit(String cuit);
}
