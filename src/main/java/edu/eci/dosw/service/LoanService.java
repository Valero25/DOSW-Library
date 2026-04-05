package edu.eci.dosw.service;

import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.model.Availability;
import edu.eci.dosw.model.Book;
import edu.eci.dosw.model.Loan;
import edu.eci.dosw.model.LoanHistory;
import edu.eci.dosw.model.User;
import edu.eci.dosw.persistence.BookPersistenceRepository;
import edu.eci.dosw.persistence.LoanPersistenceRepository;
import edu.eci.dosw.persistence.UserPersistenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de gestionar los préstamos de la biblioteca.
 */
@Service
public class LoanService {

    private final LoanPersistenceRepository loanRepository;
    private final BookPersistenceRepository bookRepository;
    private final UserPersistenceRepository userRepository;

    public LoanService(LoanPersistenceRepository loanRepository,
                       BookPersistenceRepository bookRepository,
                       UserPersistenceRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    /**
     * Crea un préstamo de un libro para un usuario.
     */
    @Transactional
    public Loan loanBook(String bookId, String userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + bookId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));

        Availability avail = book.getAvailability();
        int available = (avail != null && avail.getAvailableCopies() != null) ? avail.getAvailableCopies() : 0;
        if (available <= 0) {
            throw new BookNotAvailableException(bookId);
        }

        // Reducir copias disponibles
        if (avail == null) {
            avail = new Availability();
            book.setAvailability(avail);
        }
        avail.setAvailableCopies(available - 1);
        int loaned = avail.getLoanedCopies() != null ? avail.getLoanedCopies() : 0;
        avail.setLoanedCopies(loaned + 1);
        book.setAvailable(avail.getAvailableCopies() > 0);
        avail.setStatus(avail.getAvailableCopies() > 0 ? "Disponible" : "Agotado");
        bookRepository.save(book);

        // Crear préstamo
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setReturned(false);

        // Agregar historial
        LoanHistory history = new LoanHistory("Prestado", LocalDate.now());
        loan.setLoanHistory(new ArrayList<>(List.of(history)));

        return loanRepository.save(loan);
    }

    /**
     * Registra la devolución de un libro.
     */
    @Transactional
    public Loan returnBook(String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado: " + loanId));

        if (loan.isReturned()) {
            throw new IllegalArgumentException("El préstamo " + loanId + " ya fue devuelto.");
        }

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        // Incrementar copias disponibles del libro
        Book book = loan.getBook();
        if (book != null) {
            Availability avail = book.getAvailability();
            int available = (avail != null && avail.getAvailableCopies() != null) ? avail.getAvailableCopies() : 0;
            int total = (avail != null && avail.getTotalCopies() != null) ? avail.getTotalCopies() : 0;

            if (available < total) {
                if (avail == null) {
                    avail = new Availability();
                    book.setAvailability(avail);
                }
                avail.setAvailableCopies(available + 1);
                int loanedCopies = avail.getLoanedCopies() != null ? avail.getLoanedCopies() : 0;
                avail.setLoanedCopies(loanedCopies - 1);
                book.setAvailable(true);
                avail.setStatus("Disponible");
                bookRepository.save(book);
            }
        }

        // Agregar historial de devolución
        LoanHistory history = new LoanHistory("Devuelto", LocalDate.now());
        if (loan.getLoanHistory() == null) {
            loan.setLoanHistory(new ArrayList<>());
        }
        loan.getLoanHistory().add(history);

        return loanRepository.save(loan);
    }

    /**
     * Retorna todos los préstamos.
     */
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    /**
     * Retorna los préstamos de un usuario específico.
     */
    public List<Loan> getLoansByUserId(String userId) {
        return loanRepository.findByUserId(userId);
    }
}
