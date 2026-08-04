package com.app.TPreservasturisticas.service;

import com.app.TPreservasturisticas.dto.AgregarPagoDTO;
import com.app.TPreservasturisticas.dto.AgregarReservaDTO;
import com.app.TPreservasturisticas.entity.*;
import com.app.TPreservasturisticas.exception.NoEncontradoException;
import com.app.TPreservasturisticas.exception.ReglaNegocioException;
import com.app.TPreservasturisticas.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {
    private ReservaRepository reservaRepository;
    private ClienteRepository clienteRepository;
    private ActividadRepository actividadRepository;
    private PagoRepository pagoRepository;
    private GuiaRepository guiaRepository;
    private ActividadService actividadService;
    private static final int DIAS_PLAZO_PAGO = 7;

    public ReservaService(ReservaRepository reservaRepository,
                          ClienteRepository clienteRepository,
                          ActividadRepository actividadRepository,
                          PagoRepository pagoRepository,
                          GuiaRepository guiaRepository,
                          ActividadService actividadService) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.actividadRepository = actividadRepository;
        this.pagoRepository = pagoRepository;
        this.guiaRepository = guiaRepository;
        this.actividadService = actividadService;
    }

    //POST
    public Reserva agregar(AgregarReservaDTO reservaDTO) {
        Cliente cliente = clienteRepository.findById(reservaDTO.idCliente())
                .orElseThrow(() -> new NoEncontradoException("no existe el cliente " + reservaDTO.idCliente()));

        Actividad actividad = actividadRepository.findById(reservaDTO.idActividad())
                .orElseThrow(() -> new NoEncontradoException("no existe la actividad " + reservaDTO.idActividad()));

        if (reservaDTO.cantidadPersonas() == null || reservaDTO.cantidadPersonas() <= 0) {
            throw new IllegalArgumentException("la cantidad de personas tiene que ser mayor a 0");
        }

        List<Reserva> reservasPrevias = reservaRepository.findByCliente_IdAndActividad_IdAndEstadoNot(
                cliente.getId(), actividad.getId(), EstadoReserva.CANCELADA);

        if(!reservasPrevias.isEmpty()) {
            throw new ReglaNegocioException("el cliente ya tiene una reserva para esta actividad");
        }

        int ocupada = actividadService.getCapacidadOcupada(actividad.getId());
        int restante = actividad.getCapacidadMaxima() - ocupada;

        Double precio = calcularPrecio(actividad, reservaDTO.cantidadPersonas());

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setActividad(actividad);
        reserva.setCantidadPersonas(reservaDTO.cantidadPersonas());
        reserva.setFechaCreacion(LocalDateTime.now());
        reserva.setPrecioTotal(precio);

        if (reservaDTO.cantidadPersonas() <= restante) {

            // si la actividad requiere guia, busco uno disponible. si no hay, mando a lista de espera
            if (Boolean.TRUE.equals(actividad.getRequiereGuia())) {
                List<Guia> disponibles = guiaRepository.findByDisponibleTrue();
                if (disponibles.isEmpty()) {
                    reserva.setEstado(EstadoReserva.LISTA_ESPERA);
                    return reservaRepository.save(reserva);
                }
                Guia guia = disponibles.get(0);
                guia.setDisponible(false);
                guiaRepository.save(guia);
                reserva.setGuiaAsignado(guia);
            }

            reserva.setEstado(EstadoReserva.CONFIRMADA);
        } else {
            reserva.setEstado(EstadoReserva.LISTA_ESPERA);
        }

        return reservaRepository.save(reserva);
    }

    //GET
    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new NoEncontradoException("no existe la reserva"));
    }

    public List<Reserva> obtenerTodas() {
        return reservaRepository.findAll();
    }

    //OTROS REQUERIMIENTOS
    public void cancelarReserva(Long id) {
        Reserva reserva = obtenerPorId(id);

        if (reserva.getEstado() == EstadoReserva.PAGADA) {
            throw new ReglaNegocioException("una reserva pagada no se puede cancelar");
        }
        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new ReglaNegocioException("la reserva ya estaba cancelada");
        }

        // si tenia guia asignado, lo libero
        if (reserva.getGuiaAsignado() != null) {
            Guia guia = reserva.getGuiaAsignado();
            guia.setDisponible(true);
            guiaRepository.save(guia);
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);

        // libero capacidad y trato de promover gente de la lista de espera
        promoverListaEspera(reserva.getActividad().getId());
    }

    public Reserva registrarPago(Long idReserva, AgregarPagoDTO pagoDTO) {
        Reserva reserva = obtenerPorId(idReserva);

        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new ReglaNegocioException("solo se puede pagar una reserva confirmada");
        }

        // plazo de N dias
        LocalDate limitePago = reserva.getActividad().getFecha().plusDays(DIAS_PLAZO_PAGO);
        if (LocalDate.now().isAfter(limitePago)) {
            throw new ReglaNegocioException("se vencio el plazo para pagar esta reserva");
        }

        MetodoPago metodo;
        try {
            metodo = MetodoPago.valueOf(pagoDTO.metodoPago().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("metodo de pago invalido, tiene que ser EFECTIVO o TRANSFERENCIA");
        }

        if (metodo == MetodoPago.TRANSFERENCIA) {
            if (pagoDTO.cuitTitular() == null || pagoDTO.cuitTitular().isBlank()
                    || pagoDTO.nombreTitular() == null || pagoDTO.nombreTitular().isBlank()) {
                throw new IllegalArgumentException("para transferencia hay que informar cuit y titular");
            }
        }

        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setMetodo(metodo);
        pago.setMonto(reserva.getPrecioTotal());
        pago.setFecha(LocalDateTime.now());
        pago.setCuitTitular(pagoDTO.cuitTitular());
        pago.setNombreTitular(pagoDTO.nombreTitular());
        pagoRepository.save(pago);

        reserva.setEstado(EstadoReserva.PAGADA);
        reserva.setPago(pago);
        return reservaRepository.save(reserva);
    }

    public void vencerReservasNoPagadas() {
        List<Reserva> confirmadas = reservaRepository.findAll();
        for (Reserva r : confirmadas) {
            if (r.getEstado() == EstadoReserva.CONFIRMADA) {
                LocalDate limite = r.getActividad().getFecha().plusDays(DIAS_PLAZO_PAGO);
                if (LocalDate.now().isAfter(limite)) {
                    r.setEstado(EstadoReserva.VENCIDA);
                    if (r.getGuiaAsignado() != null) {
                        r.getGuiaAsignado().setDisponible(true);
                        guiaRepository.save(r.getGuiaAsignado());
                    }
                    reservaRepository.save(r);
                    promoverListaEspera(r.getActividad().getId());
                }
            }
        }
    }

    private void promoverListaEspera(Long idActividad) {
        Actividad actividad = actividadRepository.findById(idActividad).orElseThrow();
        List<Reserva> enEspera = reservaRepository.findByActividad_IdAndEstadoOrderByFechaCreacionAsc(
                idActividad, EstadoReserva.LISTA_ESPERA);

        int restante = actividad.getCapacidadMaxima() - actividadService.getCapacidadOcupada(idActividad);

        for (Reserva r : enEspera) {
            if (r.getCantidadPersonas() <= restante) {
                r.setEstado(EstadoReserva.CONFIRMADA);
                reservaRepository.save(r);
                restante -= r.getCantidadPersonas();
            }
        }
    }

    private Double calcularPrecio(Actividad actividad, int cantidadPersonas) {
        Double precio = actividad.getPrecioBase() * cantidadPersonas;

        if (cantidadPersonas > actividad.getUmbralDescuento()) {
            Double descuento = (precio * actividad.getPorcentajeDescuento()) / 100.0;
            precio = precio - descuento;
        }

        return precio;
    }
}
