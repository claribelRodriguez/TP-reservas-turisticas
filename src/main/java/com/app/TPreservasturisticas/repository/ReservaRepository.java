package com.app.TPreservasturisticas.repository;

import com.app.TPreservasturisticas.entity.EstadoReserva;
import com.app.TPreservasturisticas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("SELECT COALESCE(SUM(r.cantidadPersonas), 0) FROM Reserva r WHERE r.actividad.id = :idActividad AND r.estado = :estado")
    Integer getCantidadPersonasReservaPorEstado(@Param("idActividad") Long idActividad, @Param("estado") EstadoReserva estado);

    //ver si el cliente no tiene una reserva para la misma actividad / fecha
    List<Reserva> findByCliente_IdAndActividad_IdAndEstadoNot(Long idCliente, Long idActividad, EstadoReserva estadoExcluido);

    List<Reserva> findByActividad_IdAndEstadoOrderByFechaCreacionAsc(Long idActividad, EstadoReserva estado);
}
