package com.app.TPreservasturisticas.repository;

import com.app.TPreservasturisticas.entity.EstadoReserva;
import com.app.TPreservasturisticas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("SELECT SUM(r.cantidadPersonas) FROM Reserva r WHERE r.actividad = :idActividad AND r.estado = :estado")
    int getCantidadPersonasReservaPorEstado(Long idActividad, EstadoReserva estado);

    //ver si el cliente no tiene una reserva para la misma actividad / fecha
    List<Reserva> findById_ClienteAndId_ActividadAndEstadoNot(Long idCliente, Long idActividad, EstadoReserva estadoExcluido);

    List<Reserva> findByActividad_IdAndEstadoOrderByFechaCreacionAsc(Long idActividad, EstadoReserva estado);
}
