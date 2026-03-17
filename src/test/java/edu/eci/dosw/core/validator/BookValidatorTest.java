package edu.eci.dosw.core.validator;

import edu.eci.dosw.core.model.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link BookValidator}.
 */
class BookValidatorTest {

    @Test
    void validar_DebePasar_CuandoLibroEsValido() {
        Book libro = new Book("1", "Clean Code", "Robert C. Martin", "9780132350884");
        assertDoesNotThrow(() -> BookValidator.validate(libro));
    }

    @Test
    void validar_DebeLanzarExcepcion_CuandoLibroEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> BookValidator.validate(null));
    }

    @Test
    void validar_DebeLanzarExcepcion_CuandoTituloEstaVacio() {
        Book libro = new Book("1", "", "Autor", "9780132350884");
        assertThrows(IllegalArgumentException.class, () -> BookValidator.validate(libro));
    }

    @Test
    void validar_DebeLanzarExcepcion_CuandoAutorEstaVacio() {
        Book libro = new Book("1", "Título", "", "9780132350884");
        assertThrows(IllegalArgumentException.class, () -> BookValidator.validate(libro));
    }
}
