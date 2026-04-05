package edu.eci.dosw.core.service;

import edu.eci.dosw.core.model.Reserva;
import edu.eci.dosw.core.util.IdGeneratorUtil;
import edu.eci.dosw.core.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar las reservas de libros en la biblioteca.
 * Administra la creación, consulta y eliminación de reservas.
 */
@Service("coreReservaService")
public class ReservaService {

    /**
     * Lista que almacena todas las reservas registradas en el sistema.
     */
    private final List<Reserva> reservas = new java.util.ArrayList<>();

    /**
     * Crea una nueva reserva en el sistema.
     * Si la reserva no posee identificador, se genera uno automáticamente.
     *
     * @param reserva reserva a registrar en el sistema
     * @return la reserva creada con su identificador asignado
     * @throws IllegalArgumentException si la reserva es nula o los datos requeridos están vacíos
     */
    public Reserva crearReserva(Reserva reserva) {
        ValidationUtil.validateNotNull(reserva, "reserva");
        ValidationUtil.validateNotBlank(reserva.getUserId(), "userId");
        ValidationUtil.validateNotBlank(reserva.getBookId(), "bookId");

        if (reserva.getId() == null || reserva.getId().isBlank()) {
            reserva.setId(IdGeneratorUtil.generate());
        }
        if (reserva.getReservationDate() == null) {
            reserva.setReservationDate(java.time.LocalDate.now());
        }
        if (reserva.getStatus() == null || reserva.getStatus().isBlank()) {
            reserva.setStatus("PENDIENTE");
        }

        reservas.add(reserva);
        return reserva;
    }

    /**
     * Obtiene una reserva por su identificador único.
     *
     * @param id identificador de la reserva
     * @return un {@link Optional} con la reserva si existe, o vacío si no se encuentra
     */
    public Optional<Reserva> obtenerReservaPorId(String id) {
        ValidationUtil.validateNotBlank(id, "id");
        return reservas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    /**
     * Retorna todas las reservas registradas en el sistema.
     *
     * @return lista de todas las reservas
     */
    public List<Reserva> obtenerTodasLasReservas() {
        return reservas.stream().collect(Collectors.toList());
    }

    /**
     * Obtiene todas las reservas registradas para un usuario específico.
     *
     * @param userId identificador del usuario
     * @return lista de reservas del usuario
     */
    public List<Reserva> obtenerReservasPorUsuario(String userId) {
        ValidationUtil.validateNotBlank(userId, "userId");
        return reservas.stream()
                .filter(r -> r.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las reservas registradas para un libro específico.
     *
     * @param bookId identificador del libro
     * @return lista de reservas del libro
     */
    public List<Reserva> obtenerReservasPorLibro(String bookId) {
        ValidationUtil.validateNotBlank(bookId, "bookId");
        return reservas.stream()
                .filter(r -> r.getBookId().equals(bookId))
                .collect(Collectors.toList());
    }

    /**
     * Elimina una reserva del sistema por su identificador.
     *
     * @param id identificador de la reserva a eliminar
     * @return {@code true} si la reserva fue eliminada; {@code false} si no se encontró
     */
    public boolean eliminarReserva(String id) {
        ValidationUtil.validateNotBlank(id, "id");
        return reservas.removeIf(r -> r.getId().equals(id));
    }

    /**
     * Actualiza el estado de una reserva existente.
     *
     * @param id identificador de la reserva
     * @param nuevoEstado nuevo estado de la reserva
     * @throws IllegalArgumentException si la reserva no existe o el estado está vacío
     */
    public void actualizarEstadoReserva(String id, String nuevoEstado) {
        ValidationUtil.validateNotBlank(id, "id");
        ValidationUtil.validateNotBlank(nuevoEstado, "nuevoEstado");

        Reserva reserva = obtenerReservaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada: " + id));
        reserva.setStatus(nuevoEstado);
    }

    /**
     * Retorna la cantidad total de reservas en el sistema.
     *
     * @return número de reservas registradas
     */
    public int contarReservas() {
        return reservas.size();
    }

    /**
     * Elimina todas las reservas del sistema. Útil para pruebas.
     */
    public void limpiarTodasLasReservas() {
        reservas.clear();
    }
}
