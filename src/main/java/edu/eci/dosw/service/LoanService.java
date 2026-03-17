package edu.eci.dosw.service;

import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.model.Book;
import edu.eci.dosw.model.Loan;
import edu.eci.dosw.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service class encapsulating loan-related operations.
 */
@Service
public class LoanService {

    private final BookService bookService;
    private final UserService userService;
    private final List<Loan> loans = new ArrayList<>();

    public LoanService(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    /**
     * Creates a loan for the specified book and user.
     *
     * @param bookId the ID of the book to loan
     * @param userId the ID of the user requesting the loan
     * @return the created Loan
     * @throws BookNotAvailableException if the book is not available
     * @throws IllegalArgumentException  if the book or user is not found
     */
    public Loan loanBook(String bookId, String userId) {
        Book book = bookService.findBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException(bookId);
        }

        book.setAvailable(false);
        Loan loan = new Loan(UUID.randomUUID().toString(), book, user, LocalDate.now());
        loans.add(loan);
        return loan;
    }

    /**
     * Returns a book, marking the associated loan as complete.
     *
     * @param loanId the ID of the loan to complete
     * @return the updated Loan
     * @throws IllegalArgumentException if the loan is not found or already returned
     */
    public Loan returnBook(String loanId) {
        Loan loan = loans.stream()
                .filter(l -> l.getId().equals(loanId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));

        if (loan.isReturned()) {
            throw new IllegalArgumentException("Loan " + loanId + " has already been returned.");
        }

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());
        loan.getBook().setAvailable(true);
        return loan;
    }

    /**
     * Returns all loans in the system.
     *
     * @return list of all loans
     */
    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }
}
