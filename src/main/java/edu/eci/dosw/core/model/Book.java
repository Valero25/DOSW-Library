package edu.eci.dosw.core.model;

/**
 * Representa un libro en el catálogo de la biblioteca.
 * Contiene la información básica del libro y su estado de disponibilidad.
 */
public class Book {

    private String id;
    private String title;
    private String author;
    private String isbn;
    private boolean available;

    /**
     * Constructor por defecto.
     */
    public Book() {
    }

    /**
     * Constructor con todos los parámetros requeridos.
     *
     * @param id     identificador único del libro
     * @param title  título del libro
     * @param author autor del libro
     * @param isbn   código ISBN del libro
     */
    public Book(String id, String title, String author, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = true;
    }

    /**
     * Retorna el identificador único del libro.
     *
     * @return identificador del libro
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único del libro.
     *
     * @param id identificador del libro
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna el título del libro.
     *
     * @return título del libro
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título del libro.
     *
     * @param title título del libro
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retorna el nombre del autor del libro.
     *
     * @return autor del libro
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Establece el nombre del autor del libro.
     *
     * @param author autor del libro
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Retorna el código ISBN del libro.
     *
     * @return ISBN del libro
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Establece el código ISBN del libro.
     *
     * @param isbn ISBN del libro
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Indica si el libro está disponible para préstamo.
     *
     * @return {@code true} si el libro está disponible; {@code false} en caso contrario
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Establece la disponibilidad del libro.
     *
     * @param available {@code true} para marcar el libro como disponible
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Retorna una representación en cadena del libro.
     *
     * @return representación textual del objeto
     */
    @Override
    public String toString() {
        return "Book{id='" + id + "', title='" + title + "', author='" + author
                + "', isbn='" + isbn + "', available=" + available + "}";
    }
}
