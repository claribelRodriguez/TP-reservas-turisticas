package com.app.TPreservasturisticas.repository;

import com.app.TPreservasturisticas.entity.EstadoReserva;
import com.app.TPreservasturisticas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("SELECT COALESCE(SUM(r.cantidadPersonas), 0) FROM Reserva r WHERE r.experiencia.id = :idExperiencia AND r.estado = :estado")
    Integer getCantidadPersonasReservaPorEstado(@Param("experienciaId") Long actividadId, @Param("estado") EstadoReserva estado);

    @Query("SELECT r FROM Reserva r WHERE r.estado = :estado")
    List<Reserva> getReservasPorEstado(@Param("estado") EstadoReserva estado);

    //ver si el cliente no tiene una reserva para la misma actividad / fecha
    List<Reserva> findByCliente_IdAndExperiencia_IdAndEstadoNot(Long clienteId, Long experienciaId, EstadoReserva estadoExcluido);

    List<Reserva> findByExperiencia_IdAndEstadoOrderByFechaCreacionAsc(Long actividaId, EstadoReserva estado);
}
