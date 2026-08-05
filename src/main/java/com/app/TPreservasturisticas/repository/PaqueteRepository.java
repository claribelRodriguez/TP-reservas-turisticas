package com.app.TPreservasturisticas.repository;

import com.app.TPreservasturisticas.entity.Actividad;
import com.app.TPreservasturisticas.entity.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaqueteRepository extends JpaRepository<Paquete, Long> {
    @Query("SELECT (a.id) FROM Paquete p JOIN p.actividades a WHERE p.id = :paqueteId AND a.id = :actividadId")
    Long obtenerActividadPaquete(@Param("paqueteId") Long paqueteId, @Param("actividadId") Long actividadId);
}
