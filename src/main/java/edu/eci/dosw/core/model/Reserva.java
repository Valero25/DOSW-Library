package edu.eci.dosw.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Representa una reserva de libro en el sistema de la biblioteca.
 * Una reserva vincula un usuario con un libro a una fecha específica.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    private String id;
    private String userId;
    private String bookId;
    private LocalDate reservationDate;
    private String status; // PENDIENTE, CONFIRMADA, CANCELADA

    /**
     * Constructor con parámetros principales.
     *
     * @param id identificador único de la reserva
     * @param userId identificador del usuario
     * @param bookId identificador del libro
     */
    public Reserva(String id, String userId, String bookId) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.reservationDate = LocalDate.now();
        this.status = "PENDIENTE";
    }
}
