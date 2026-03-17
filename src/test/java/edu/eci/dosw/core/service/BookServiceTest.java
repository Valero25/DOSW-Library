package edu.eci.dosw.core.service;

import edu.eci.dosw.core.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link BookService} del paquete core,
 * verificando los escenarios exitosos y de error estipulados en el diseño.
 */
class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
    }

    // --- ESCENARIOS EXITOSOS ---

    @Test
    void agregarLibro_DebeRegistrarseExitosamente_YGuardarEjemplares() {
        Book libro = new Book(null, "Clean Code", "Robert C. Martin", "9780132350884");
        Book resultado = bookService.addBook(libro, 5);

        assertNotNull(resultado.getId());
        assertEquals("Clean Code", resultado.getTitle());
        assertTrue(resultado.isAvailable());
        assertEquals(5, bookService.getAvailableCopies(resultado.getId()));
    }

    @Test
    void agregarCopias_DebeAumentarEjemplaresCorrectamente() {
        Book libro = bookService.addBook(new Book(null, "Clean Code", "Robert C. Martin", "1234567890123"), 2);
        bookService.addCopies(libro.getId(), 3);
        assertEquals(5, bookService.getAvailableCopies(libro.getId()));
    }

    @Test
    void consultarEjemplaresDisponibles_DebeRetornarCantidadExacta() {
        Book libro = bookService.addBook(new Book(null, "Libro A", "Autor A", "1111111111111"), 10);
        assertEquals(10, bookService.getAvailableCopies(libro.getId()));
    }

    @Test
    void decrementarEjemplares_DebeReducirEnUno_YMarcarNoDisponibleSiLlegaACero() {
        Book libro = bookService.addBook(new Book(null, "Libro B", "Autor", "2222222222222"), 1);
        bookService.decrementCopies(libro.getId());
        assertEquals(0, bookService.getAvailableCopies(libro.getId()));
        assertFalse(libro.isAvailable());
    }

    @Test
    void incrementarEjemplares_DebeAumentarEnUno_YMarcarDisponible() {
        Book libro = bookService.addBook(new Book(null, "Libro C", "Autor", "3333333333333"), 1);
        bookService.decrementCopies(libro.getId()); // Llega a 0
        bookService.incrementCopies(libro.getId()); // Sube a 1
        assertEquals(1, bookService.getAvailableCopies(libro.getId()));
        assertTrue(libro.isAvailable());
    }

    @Test
    void obtenerTodosLosLibros_DebeRetornarSoloLlaves() {
        bookService.addBook(new Book(null, "L1", "A1", "123"), 1);
        bookService.addBook(new Book(null, "L2", "A2", "456"), 2);
        List<Book> todos = bookService.getAllBooks();
        assertEquals(2, todos.size());
    }

    @Test
    void buscarLibroPorId_DebeRetornarLibro_CuandoExiste() {
        Book libro = bookService.addBook(new Book(null, "DDD", "Eric Evans", "9780321125217"), 1);
        Optional<Book> encontrado = bookService.findBookById(libro.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("DDD", encontrado.get().getTitle());
    }

    @Test
    void eliminarLibro_DebeRetornarVerdaderoYBorrarlo_CuandoExiste() {
        Book libro = bookService.addBook(new Book(null, "Eliminar", "Autor", "000"), 1);
        assertTrue(bookService.removeBook(libro.getId()));
        assertTrue(bookService.getAllBooks().isEmpty());
    }

    // --- ESCENARIOS DE ERROR ---

    @Test
    void agregarLibro_DebeLanzarExcepcion_CuandoLibroOAtributosSonNulosOVacios() {
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(null, 1));
        assertThrows(IllegalArgumentException.class,
                () -> bookService.addBook(new Book(null, "", "Autor", "123"), 1));
    }

    @Test
    void agregarLibro_DebeLanzarExcepcion_CuandoCopiasSonCeroOMenores() {
        Book libro = new Book(null, "T", "A", "1");
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(libro, 0));
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(libro, -1));
    }

    @Test
    void agregarCopias_DebeLanzarExcepcion_CuandoLibroNoExisteOCopiasInvalidas() {
        Book libro = bookService.addBook(new Book(null, "T", "A", "1"), 1);
        assertThrows(IllegalArgumentException.class, () -> bookService.addCopies("falso", 1));
        assertThrows(IllegalArgumentException.class, () -> bookService.addCopies(libro.getId(), 0));
    }

    @Test
    void decrementarEjemplares_DebeLanzarExcepcion_CuandoYaEsCero() {
        Book libro = bookService.addBook(new Book(null, "T", "A", "1"), 1);
        bookService.decrementCopies(libro.getId());
        assertThrows(IllegalArgumentException.class, () -> bookService.decrementCopies(libro.getId()));
    }
}
