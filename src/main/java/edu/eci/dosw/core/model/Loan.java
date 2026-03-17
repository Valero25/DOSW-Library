package edu.eci.dosw.core.model;

import java.time.LocalDate;

/**
 * Representa un préstamo de libro en el sistema de la biblioteca.
 * Asocia un libro con un usuario durante un período determinado y registra su estado de devolución.
 */
public class Loan {

    private String id;
    private Book book;
    private User user;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private boolean returned;

    /**
     * Constructor por defecto.
     */
    public Loan() {
    }

    /**
     * Constructor con los parámetros esenciales para crear un préstamo activo.
     *
     * @param id       identificador único del préstamo
     * @param book     libro objeto del préstamo
     * @param user     usuario que realiza el préstamo
     * @param loanDate fecha en que se realizó el préstamo
     */
    public Loan(String id, Book book, User user, LocalDate loanDate) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.returned = false;
    }

    /**
     * Retorna el identificador único del préstamo.
     *
     * @return identificador del préstamo
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único del préstamo.
     *
     * @param id identificador del préstamo
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna el libro asociado al préstamo.
     *
     * @return libro del préstamo
     */
    public Book getBook() {
        return book;
    }

    /**
     * Establece el libro asociado al préstamo.
     *
     * @param book libro del préstamo
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Retorna el usuario que realizó el préstamo.
     *
     * @return usuario del préstamo
     */
    public User getUser() {
        return user;
    }

    /**
     * Establece el usuario que realizó el préstamo.
     *
     * @param user usuario del préstamo
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retorna la fecha en que se realizó el préstamo.
     *
     * @return fecha del préstamo
     */
    public LocalDate getLoanDate() {
        return loanDate;
    }

    /**
     * Establece la fecha de realización del préstamo.
     *
     * @param loanDate fecha del préstamo
     */
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    /**
     * Retorna la fecha en que se devolvió el libro.
     *
     * @return fecha de devolución, o {@code null} si aún no ha sido devuelto
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Establece la fecha de devolución del libro.
     *
     * @param returnDate fecha de devolución
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * Indica si el préstamo ha sido completado mediante la devolución del libro.
     *
     * @return {@code true} si el libro fue devuelto; {@code false} si el préstamo sigue activo
     */
    public boolean isReturned() {
        return returned;
    }

    /**
     * Establece el estado de devolución del préstamo.
     *
     * @param returned {@code true} para marcar el préstamo como devuelto
     */
    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    /**
     * Retorna una representación en cadena del préstamo.
     *
     * @return representación textual del objeto
     */
    @Override
    public String toString() {
        return "Loan{id='" + id + "', book=" + book + ", user=" + user
                + ", loanDate=" + loanDate + ", returned=" + returned + "}";
    }
}
