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
    private PagoRepository pagoRepository;
    private GuiaRepository guiaRepository;
    private ExperienciaTuristicaRepository experienciaTuristicaRepository;
    private static final int DIAS_PLAZO_PAGO = 7;

    public ReservaService(ReservaRepository reservaRepository,
                          ClienteRepository clienteRepository,
                          PagoRepository pagoRepository,
                          GuiaRepository guiaRepository,
                          ExperienciaTuristicaRepository experienciaTuristicaRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.pagoRepository = pagoRepository;
        this.guiaRepository = guiaRepository;
        this.experienciaTuristicaRepository = experienciaTuristicaRepository;
    }

    //POST
    public Reserva agregar(AgregarReservaDTO reservaDTO) {
        Cliente cliente = clienteRepository.findById(reservaDTO.idCliente())
                .orElseThrow(() -> new NoEncontradoException("no existe el cliente " + reservaDTO.idCliente()));

        ExperienciaTuristica experiencia = experienciaTuristicaRepository.findById(reservaDTO.idExperiencia())
                .orElseThrow(() -> new NoEncontradoException("no existe la experiencia no existe"));

        if (reservaDTO.cantidadPersonas() == null || reservaDTO.cantidadPersonas() <= 0) {
            throw new ReglaNegocioException("la cantidad de personas tiene que ser mayor a 0");
        }

        if(experiencia.getFechaInicio() == null && experiencia.getFechaFin() == null) {
            throw new ReglaNegocioException("para poder registrar la actividad debe tener las fechas de inicio y fin estipuladas");
        }

        List<Reserva> reservasPrevias = reservaRepository.findByCliente_IdAndExperiencia_IdAndEstadoNot(
                cliente.getId(), experiencia.getId(), EstadoReserva.CANCELADA);

        if(!reservasPrevias.isEmpty()) {
            throw new ReglaNegocioException("el cliente ya tiene una reserva para esta actividad");
        }

        int ocupada = getCapacidadOcupada(experiencia.getId());
        int restante = experiencia.getCapacidadMaxima() - ocupada;

        Double precio = calcularPrecio(experiencia, reservaDTO.cantidadPersonas());

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setExperiencia(experiencia);
        reserva.setCantidadPersonas(reservaDTO.cantidadPersonas());
        reserva.setFechaCreacion(LocalDateTime.now());
        reserva.setPrecioTotal(precio);

        if (reservaDTO.cantidadPersonas() <= restante) {

            // si la experiencia requiere guia, busco uno disponible. si no hay, mando a lista de espera
            if (Boolean.TRUE.equals(experiencia.getRequiereGuia())) {
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
        promoverListaEspera(reserva.getExperiencia().getId());
    }

    public Reserva registrarPago(Long idReserva, AgregarPagoDTO pagoDTO) {
        Reserva reserva = obtenerPorId(idReserva);

        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new ReglaNegocioException("solo se puede pagar una reserva confirmada");
        }

        // plazo de N dias
        LocalDate limitePago = reserva.getExperiencia().getFechaInicio().plusDays(DIAS_PLAZO_PAGO);
        if (LocalDate.now().isAfter(limitePago)) {
            throw new ReglaNegocioException("se vencio el plazo para pagar esta reserva");
        }

        if (pagoDTO.metodoPago() == MetodoPago.TRANSFERENCIA) {
            if (pagoDTO.cuitTitular() == null || pagoDTO.cuitTitular().isBlank()
                    || pagoDTO.nombreTitular() == null || pagoDTO.nombreTitular().isBlank()) {
                throw new ReglaNegocioException("para transferencia hay que informar cuit y titular");
            }
        }

        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setMetodo(pagoDTO.metodoPago());
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
        List<Reserva> confirmadas = reservaRepository.getReservasPorEstado(EstadoReserva.CONFIRMADA);
        for (Reserva r : confirmadas) {
            LocalDate limite = r.getExperiencia().getFechaInicio().plusDays(DIAS_PLAZO_PAGO);
            if (LocalDate.now().isAfter(limite)) {
                r.setEstado(EstadoReserva.VENCIDA);
                if (r.getGuiaAsignado() != null) {
                    r.getGuiaAsignado().setDisponible(true);
                    guiaRepository.save(r.getGuiaAsignado());
                }
                reservaRepository.save(r);
                promoverListaEspera(r.getExperiencia().getId());
            }
        }
    }

    private void promoverListaEspera(Long experienciaId) {
        ExperienciaTuristica experiencia = experienciaTuristicaRepository.findById(experienciaId).orElseThrow();
        List<Reserva> enEspera = reservaRepository.findByExperiencia_IdAndEstadoOrderByFechaCreacionAsc(
                experienciaId, EstadoReserva.LISTA_ESPERA);

        int restante = experiencia.getCapacidadMaxima() - getCapacidadOcupada(experienciaId);

        for (Reserva r : enEspera) {
            if (r.getCantidadPersonas() <= restante) {
                r.setEstado(EstadoReserva.CONFIRMADA);
                reservaRepository.save(r);
                restante -= r.getCantidadPersonas();
            }
        }
    }

    private Double calcularPrecio(ExperienciaTuristica experiencia, int cantidadPersonas) {
        Double precio = experiencia.getPrecioBase() * cantidadPersonas;

        if (cantidadPersonas > experiencia.getUmbralDescuento()) {
            Double descuento = (precio * experiencia.getPorcentajeDescuento()) / 100.0;
            precio = precio - descuento;
        }

        return precio;
    }

    public int getCapacidadOcupada(Long idActividad) {
        //Obtengo la cantidad de personas que para una actividad su estado sea CONFIRMADA o PAGADA
        int cantPersonasReservaConfirmada = reservaRepository.getCantidadPersonasReservaPorEstado(idActividad, EstadoReserva.CONFIRMADA);
        int cantPersonasReservaPagada = reservaRepository.getCantidadPersonasReservaPorEstado(idActividad, EstadoReserva.PAGADA);
        return cantPersonasReservaConfirmada + cantPersonasReservaPagada;
    }
}
