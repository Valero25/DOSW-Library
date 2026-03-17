package edu.eci.dosw.controller.dto;

/**
 * Objeto de transferencia de datos (DTO) que representa la información de un libro
 * expuesta a través de la capa de controladores REST.
 */
public class BookDTO {

    private String id;
    private String title;
    private String author;
    private String isbn;
    private boolean available;

    /**
     * Constructor por defecto.
     */
    public BookDTO() {
    }

    /**
     * Constructor con todos los campos del DTO.
     *
     * @param id        identificador único del libro
     * @param title     título del libro
     * @param author    autor del libro
     * @param isbn      código ISBN del libro
     * @param available indicador de disponibilidad del libro
     */
    public BookDTO(String id, String title, String author, String isbn, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available;
    }

    /**
     * Retorna el identificador del libro.
     *
     * @return identificador del libro
     */
    public String getId() { return id; }

    /**
     * Establece el identificador del libro.
     *
     * @param id identificador del libro
     */
    public void setId(String id) { this.id = id; }

    /**
     * Retorna el título del libro.
     *
     * @return título del libro
     */
    public String getTitle() { return title; }

    /**
     * Establece el título del libro.
     *
     * @param title título del libro
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Retorna el autor del libro.
     *
     * @return autor del libro
     */
    public String getAuthor() { return author; }

    /**
     * Establece el autor del libro.
     *
     * @param author autor del libro
     */
    public void setAuthor(String author) { this.author = author; }

    /**
     * Retorna el código ISBN del libro.
     *
     * @return ISBN del libro
     */
    public String getIsbn() { return isbn; }

    /**
     * Establece el código ISBN del libro.
     *
     * @param isbn ISBN del libro
     */
    public void setIsbn(String isbn) { this.isbn = isbn; }

    /**
     * Indica si el libro está disponible para préstamo.
     *
     * @return {@code true} si el libro está disponible
     */
    public boolean isAvailable() { return available; }

    /**
     * Establece la disponibilidad del libro.
     *
     * @param available {@code true} si el libro está disponible
     */
    public void setAvailable(boolean available) { this.available = available; }
}
