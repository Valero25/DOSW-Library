package edu.eci.dosw.controller.dto;

import java.time.LocalDate;

/**
 * Objeto de transferencia de datos (DTO) que representa la información de un préstamo
 * expuesta a través de la capa de controladores REST.
 * Utiliza identificadores de libro y usuario en lugar de objetos anidados completos.
 */
public class LoanDTO {

    private String id;
    private String bookId;
    private String userId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private boolean returned;

    /**
     * Constructor por defecto.
     */
    public LoanDTO() {
    }

    /**
     * Constructor con todos los campos del DTO.
     *
     * @param id         identificador único del préstamo
     * @param bookId     identificador del libro prestado
     * @param userId     identificador del usuario que realizó el préstamo
     * @param loanDate   fecha de realización del préstamo
     * @param returnDate fecha de devolución; puede ser nula si aún no ha sido devuelto
     * @param returned   indicador de si el préstamo fue completado
     */
    public LoanDTO(String id, String bookId, String userId,
                   LocalDate loanDate, LocalDate returnDate, boolean returned) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.returned = returned;
    }

    /**
     * Retorna el identificador del préstamo.
     *
     * @return identificador del préstamo
     */
    public String getId() { return id; }

    /**
     * Establece el identificador del préstamo.
     *
     * @param id identificador del préstamo
     */
    public void setId(String id) { this.id = id; }

    /**
     * Retorna el identificador del libro prestado.
     *
     * @return identificador del libro
     */
    public String getBookId() { return bookId; }

    /**
     * Establece el identificador del libro prestado.
     *
     * @param bookId identificador del libro
     */
    public void setBookId(String bookId) { this.bookId = bookId; }

    /**
     * Retorna el identificador del usuario que realizó el préstamo.
     *
     * @return identificador del usuario
     */
    public String getUserId() { return userId; }

    /**
     * Establece el identificador del usuario que realizó el préstamo.
     *
     * @param userId identificador del usuario
     */
    public void setUserId(String userId) { this.userId = userId; }

    /**
     * Retorna la fecha de realización del préstamo.
     *
     * @return fecha del préstamo
     */
    public LocalDate getLoanDate() { return loanDate; }

    /**
     * Establece la fecha de realización del préstamo.
     *
     * @param loanDate fecha del préstamo
     */
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    /**
     * Retorna la fecha de devolución del libro.
     *
     * @return fecha de devolución, o {@code null} si no ha sido devuelto
     */
    public LocalDate getReturnDate() { return returnDate; }

    /**
     * Establece la fecha de devolución del libro.
     *
     * @param returnDate fecha de devolución
     */
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    /**
     * Indica si el préstamo ha sido finalizado mediante la devolución del libro.
     *
     * @return {@code true} si el libro fue devuelto
     */
    public boolean isReturned() { return returned; }

    /**
     * Establece el estado de devolución del préstamo.
     *
     * @param returned {@code true} para indicar que el libro fue devuelto
     */
    public void setReturned(boolean returned) { this.returned = returned; }
}
