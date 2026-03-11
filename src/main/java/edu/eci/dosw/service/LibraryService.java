package edu.eci.dosw.service;

import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.model.Book;
import edu.eci.dosw.model.Loan;
import edu.eci.dosw.model.User;
import edu.eci.dosw.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class encapsulating the core business logic of the library system.
 */
@Service
public class LibraryService {

    private final List<Book> books = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<Loan> loans = new ArrayList<>();

    // ----------------------------- Books -----------------------------

    /**
     * Adds a new book to the library catalog.
     *
     * @param book the book to add
     * @return the added book
     */
    public Book addBook(Book book) {
        ValidationUtil.validateNotNull(book, "book");
        ValidationUtil.validateNotBlank(book.getTitle(), "title");
        ValidationUtil.validateNotBlank(book.getAuthor(), "author");
        if (book.getId() == null || book.getId().isBlank()) {
            book.setId(UUID.randomUUID().toString());
        }
        books.add(book);
        return book;
    }

    /**
     * Returns all books in the catalog.
     *
     * @return list of all books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Finds a book by its ID.
     *
     * @param id the book ID
     * @return Optional containing the book if found
     */
    public Optional<Book> findBookById(String id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    /**
     * Removes a book from the catalog.
     *
     * @param id the ID of the book to remove
     * @return true if removed, false if not found
     */
    public boolean removeBook(String id) {
        return books.removeIf(b -> b.getId().equals(id));
    }

    // ----------------------------- Users -----------------------------

    /**
     * Registers a new user in the system.
     *
     * @param user the user to register
     * @return the registered user
     */
    public User registerUser(User user) {
        ValidationUtil.validateNotNull(user, "user");
        ValidationUtil.validateNotBlank(user.getName(), "name");
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(UUID.randomUUID().toString());
        }
        users.add(user);
        return user;
    }

    /**
     * Returns all registered users.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the user ID
     * @return Optional containing the user if found
     */
    public Optional<User> findUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    // ----------------------------- Loans -----------------------------

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
        Book book = findBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        User user = findUserById(userId)
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
