package edu.eci.dosw.core.validator;

import edu.eci.dosw.core.model.Book;
import edu.eci.dosw.core.util.ValidationUtil;

/**
 * Validador de la entidad {@link Book}.
 * Centraliza las reglas de validación aplicables a los libros antes de ser procesados por los servicios.
 */
public class BookValidator {

    private BookValidator() {
        // Clase utilitaria — no se permite instanciar
    }

    /**
     * Valida que un libro cumpla con los requisitos mínimos del sistema.
     * Verifica que el objeto no sea nulo y que los campos título y autor estén presentes.
     *
     * @param book libro a validar
     * @throws IllegalArgumentException si el libro es nulo, o si el título o autor están vacíos
     */
    public static void validate(Book book) {
        ValidationUtil.validateNotNull(book, "libro");
        ValidationUtil.validateNotBlank(book.getTitle(), "título");
        ValidationUtil.validateNotBlank(book.getAuthor(), "autor");
    }
}
