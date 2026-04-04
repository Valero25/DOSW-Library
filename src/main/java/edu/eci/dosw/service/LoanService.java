package edu.eci.dosw.service;

import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.model.Loan;
import edu.eci.dosw.persistence.entity.BookEntity;
import edu.eci.dosw.persistence.entity.LoanEntity;
import edu.eci.dosw.persistence.entity.LoanHistoryEntity;
import edu.eci.dosw.persistence.entity.UserEntity;
import edu.eci.dosw.persistence.mapper.LoanEntityMapper;
import edu.eci.dosw.persistence.repository.BookRepository;
import edu.eci.dosw.persistence.repository.LoanRepository;
import edu.eci.dosw.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar los préstamos de la biblioteca.
 */
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    /**
     * Crea un préstamo de un libro para un usuario.
     */
    @Transactional
    public Loan loanBook(String bookId, String userId) {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + bookId));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));

        int available = book.getAvailableCopies() != null ? book.getAvailableCopies() : 0;
        if (available <= 0) {
            throw new BookNotAvailableException(bookId);
        }

        // Reducir copias disponibles
        book.setAvailableCopies(available - 1);
        book.setLoanedCopies((book.getLoanedCopies() != null ? book.getLoanedCopies() : 0) + 1);
        book.setAvailable(book.getAvailableCopies() > 0);
        book.setStatus(book.getAvailableCopies() > 0 ? "Disponible" : "Agotado");
        bookRepository.save(book);

        // Crear préstamo
        LoanEntity loan = new LoanEntity();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setReturned(false);

        // Agregar historial
        LoanHistoryEntity history = new LoanHistoryEntity();
        history.setStatus("Prestado");
        history.setDate(LocalDate.now());
        history.setLoan(loan);
        loan.setLoanHistory(new ArrayList<>(List.of(history)));

        LoanEntity saved = loanRepository.save(loan);
        return LoanEntityMapper.toDomain(saved);
    }

    /**
     * Registra la devolución de un libro.
     */
    @Transactional
    public Loan returnBook(String loanId) {
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado: " + loanId));

        if (loan.isReturned()) {
            throw new IllegalArgumentException("El préstamo " + loanId + " ya fue devuelto.");
        }

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        // Incrementar copias disponibles
        BookEntity book = loan.getBook();
        int available = book.getAvailableCopies() != null ? book.getAvailableCopies() : 0;
        int total = book.getTotalCopies() != null ? book.getTotalCopies() : 0;

        if (available < total) {
            book.setAvailableCopies(available + 1);
            book.setLoanedCopies((book.getLoanedCopies() != null ? book.getLoanedCopies() : 0) - 1);
            book.setAvailable(true);
            book.setStatus("Disponible");
            bookRepository.save(book);
        }

        // Agregar historial de devolución
        LoanHistoryEntity history = new LoanHistoryEntity();
        history.setStatus("Devuelto");
        history.setDate(LocalDate.now());
        history.setLoan(loan);
        loan.getLoanHistory().add(history);

        LoanEntity saved = loanRepository.save(loan);
        return LoanEntityMapper.toDomain(saved);
    }

    /**
     * Retorna todos los préstamos.
     */
    public List<Loan> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(LoanEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Retorna los préstamos de un usuario específico.
     */
    public List<Loan> getLoansByUserId(String userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(LoanEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
