package edu.eci.dosw.core.service;

import edu.eci.dosw.core.model.Book;
import edu.eci.dosw.core.model.Loan;
import edu.eci.dosw.core.model.User;
import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.exception.LoanLimitExceededException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link LoanService} del paquete core,
 * verificando los escenarios de negocio relacionados con la gestión de préstamos.
 */
class LoanServiceTest {

    private BookService bookService;
    private UserService userService;
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        userService = new UserService();
        loanService = new LoanService(bookService, userService);
    }

    // --- ESCENARIOS EXITOSOS ---

    @Test
    void prestarLibro_DebeCrearPrestamoYReducirEjemplares_CuandoLibroDisponible() {
        Book libro = bookService.addBook(new Book(null, "Clean Architecture", "Robert C. Martin", "9780134494166"), 1);
        User usuario = userService.registerUser(new User(null, "María", "maria@eci.edu.co"));

        Loan prestamo = loanService.loanBook(libro.getId(), usuario.getId());

        assertNotNull(prestamo.getId());
        assertEquals(0, bookService.getAvailableCopies(libro.getId())); // Se redujo el ejemplar
        assertFalse(libro.isAvailable()); // No hay más ejemplares
    }

    @Test
    void devolverLibro_DebeMarcarComoDevuelto_YAumentarEjemplares() {
        Book libro = bookService.addBook(new Book(null, "TDD", "Kent Beck", "9780321146533"), 1);
        User usuario = userService.registerUser(new User(null, "Pedro", "pedro@eci.edu.co"));
        Loan prestamo = loanService.loanBook(libro.getId(), usuario.getId());

        Loan devuelto = loanService.returnBook(prestamo.getId());

        assertTrue(devuelto.isReturned());
        assertEquals(1, bookService.getAvailableCopies(libro.getId())); // Se devolvió el ejemplar
        assertTrue(libro.isAvailable());
    }

    @Test
    void listarPrestamosPorUsuario_DebeRetornarSoloLosDelUsuario() {
        Book l1 = bookService.addBook(new Book(null, "L1", "A1", "1"), 5);
        Book l2 = bookService.addBook(new Book(null, "L2", "A2", "2"), 5);
        User u1 = userService.registerUser(new User(null, "U1", "u1@e.co"));
        User u2 = userService.registerUser(new User(null, "U2", "u2@e.co"));

        loanService.loanBook(l1.getId(), u1.getId());
        loanService.loanBook(l2.getId(), u1.getId());
        loanService.loanBook(l1.getId(), u2.getId());

        List<Loan> prestamosU1 = loanService.getLoansByUser(u1.getId());
        assertEquals(2, prestamosU1.size());
    }

    @Test
    void listarPrestamosActivos_DebeRetornarSoloLosNoDevueltos() {
        Book l1 = bookService.addBook(new Book(null, "L1", "A1", "1"), 5);
        User u1 = userService.registerUser(new User(null, "U1", "u1@e.co"));

        Loan p1 = loanService.loanBook(l1.getId(), u1.getId());
        Loan p2 = loanService.loanBook(l1.getId(), u1.getId());

        loanService.returnBook(p1.getId()); // Devuelve p1

        List<Loan> activos = loanService.getActiveLoans();
        assertEquals(1, activos.size());
        assertEquals(p2.getId(), activos.get(0).getId());
    }

    // --- ESCENARIOS DE ERROR ---

    @Test
    void prestarLibro_DebeLanzarExcepcion_CuandoSinEjemplaresDisponibles() {
        Book libro = bookService.addBook(new Book(null, "Refactoring", "Martin Fowler", "9780201485677"), 1);
        User u1 = userService.registerUser(new User(null, "Carlos", "carlos@eci.edu.co"));
        User u2 = userService.registerUser(new User(null, "Ana", "ana@eci.edu.co"));

        loanService.loanBook(libro.getId(), u1.getId()); // 0 ejemplares restantes

        assertThrows(BookNotAvailableException.class,
                () -> loanService.loanBook(libro.getId(), u2.getId()));
    }

    @Test
    void prestarLibro_DebeLanzarExcepcion_CuandoLibroNoExiste() {
        User usuario = userService.registerUser(new User(null, "Ana", "ana@eci.edu.co"));
        assertThrows(IllegalArgumentException.class,
                () -> loanService.loanBook("sin-libro", usuario.getId()));
    }

    @Test
    void prestarLibro_DebeLanzarExcepcion_CuandoUsuarioNoExiste() {
        Book libro = bookService.addBook(new Book(null, "DDD", "Eric Evans", "9780321125217"), 1);
        assertThrows(IllegalArgumentException.class,
                () -> loanService.loanBook(libro.getId(), "sin-usuario"));
    }

    @Test
    void prestarLibro_DebeLanzarExcepcion_CuandoUsuarioSuperaLimitePrestamosActivos() {
        User usuario = userService.registerUser(new User(null, "Luis", "luis@eci.edu.co"));
        for (int i = 0; i < 3; i++) {
            Book libro = bookService.addBook(new Book(null, "Libro " + i, "Autor", "978000000000" + i), 1);
            loanService.loanBook(libro.getId(), usuario.getId());
        }
        Book libroExtra = bookService.addBook(new Book(null, "Libro Extra", "Autor", "9780000000099"), 1);

        assertThrows(LoanLimitExceededException.class,
                () -> loanService.loanBook(libroExtra.getId(), usuario.getId()));
    }

    @Test
    void devolverLibro_DebeLanzarExcepcion_CuandoYaFueDevuelto() {
        Book libro = bookService.addBook(new Book(null, "SICP", "Abelson", "9780262510875"), 1);
        User usuario = userService.registerUser(new User(null, "Laura", "laura@eci.edu.co"));
        Loan prestamo = loanService.loanBook(libro.getId(), usuario.getId());
        loanService.returnBook(prestamo.getId());

        assertThrows(IllegalArgumentException.class,
                () -> loanService.returnBook(prestamo.getId()));
    }

    @Test
    void devolverLibro_DebeLanzarExcepcion_CuandoPrestamoNoExiste() {
        assertThrows(IllegalArgumentException.class,
                () -> loanService.returnBook("falso"));
    }
}
