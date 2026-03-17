package edu.eci.dosw.core.service;

import edu.eci.dosw.core.model.Book;
import edu.eci.dosw.core.model.Loan;
import edu.eci.dosw.core.model.User;
import edu.eci.dosw.core.util.DateUtil;
import edu.eci.dosw.core.util.IdGeneratorUtil;
import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.exception.LoanLimitExceededException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar el ciclo de vida de los préstamos de la biblioteca.
 * Coordina la interacción con el servicio de libros y el de usuarios para controlar
 * la disponibilidad de ejemplares y los límites de préstamo por usuario.
 */
@Service("coreLoanService")
public class LoanService {

    /**
     * Número máximo de préstamos activos permitidos por usuario.
     */
    private static final int MAX_LOANS_PER_USER = 3;

    private final BookService bookService;
    private final UserService userService;
    private final List<Loan> loans = new ArrayList<>();

    /**
     * Construye el servicio de préstamos con sus dependencias requeridas.
     *
     * @param bookService servicio de gestión del catálogo de libros
     * @param userService servicio de gestión de usuarios
     */
    public LoanService(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    /**
     * Crea un préstamo para el libro y usuario indicados.
     * Verifica disponibilidad de ejemplares y límite de préstamos activos por usuario.
     *
     * @param bookId identificador del libro a prestar
     * @param userId identificador del usuario que solicita el préstamo
     * @return el préstamo creado
     * @throws BookNotAvailableException  si el libro no tiene ejemplares disponibles
     * @throws LoanLimitExceededException si el usuario alcanzó el límite de préstamos activos
     * @throws IllegalArgumentException   si el libro o el usuario no existen en el sistema
     */
    public Loan loanBook(String bookId, String userId) {
        Book book = bookService.findBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + bookId));
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));

        if (bookService.getAvailableCopies(bookId) <= 0) {
            throw new BookNotAvailableException(bookId);
        }

        long activeLoans = loans.stream()
                .filter(l -> l.getUser().getId().equals(userId) && !l.isReturned())
                .count();
        if (activeLoans >= MAX_LOANS_PER_USER) {
            throw new LoanLimitExceededException(userId);
        }

        bookService.decrementCopies(bookId);
        Loan loan = new Loan(IdGeneratorUtil.generate(), book, user, DateUtil.today());
        loans.add(loan);
        return loan;
    }

    /**
     * Registra la devolución del libro correspondiente al préstamo indicado.
     * Incrementa el contador de ejemplares disponibles del libro.
     *
     * @param loanId identificador del préstamo a completar
     * @return el préstamo actualizado con estado de devuelto
     * @throws IllegalArgumentException si el préstamo no existe o ya fue devuelto previamente
     */
    public Loan returnBook(String loanId) {
        Loan loan = loans.stream()
                .filter(l -> l.getId().equals(loanId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado: " + loanId));

        if (loan.isReturned()) {
            throw new IllegalArgumentException("El préstamo '" + loanId + "' ya fue devuelto anteriormente.");
        }

        loan.setReturned(true);
        loan.setReturnDate(DateUtil.today());
        bookService.incrementCopies(loan.getBook().getId());
        return loan;
    }

    /**
     * Retorna todos los préstamos registrados en el sistema.
     *
     * @return lista con todos los préstamos
     */
    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    /**
     * Retorna todos los préstamos activos (no devueltos) del sistema.
     *
     * @return lista de préstamos activos
     */
    public List<Loan> getActiveLoans() {
        return loans.stream()
                .filter(l -> !l.isReturned())
                .collect(Collectors.toList());
    }

    /**
     * Retorna todos los préstamos asociados a un usuario específico.
     *
     * @param userId identificador del usuario
     * @return lista de préstamos del usuario; vacía si no tiene préstamos
     */
    public List<Loan> getLoansByUser(String userId) {
        return loans.stream()
                .filter(l -> l.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }
}
