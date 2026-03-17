package edu.eci.dosw.service;

import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.model.Book;
import edu.eci.dosw.model.Loan;
import edu.eci.dosw.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void loanBook_ShouldCreateLoan_WhenBookAndUserExist() {
        Book book = bookService.addBook(new Book(null, "Clean Architecture", "Robert C. Martin", "9780134494166"));
        User user = userService.registerUser(new User(null, "Maria", "maria@eci.edu.co"));

        Loan loan = loanService.loanBook(book.getId(), user.getId());

        assertNotNull(loan.getId());
        assertFalse(loan.getBook().isAvailable());
    }

    @Test
    void loanBook_ShouldThrowBookNotAvailableException_WhenAlreadyLoaned() {
        Book book = bookService.addBook(new Book(null, "Refactoring", "Martin Fowler", "9780201485677"));
        User user = userService.registerUser(new User(null, "Carlos", "carlos@eci.edu.co"));

        loanService.loanBook(book.getId(), user.getId());

        assertThrows(BookNotAvailableException.class,
                () -> loanService.loanBook(book.getId(), user.getId()));
    }

    @Test
    void loanBook_ShouldThrowException_WhenBookNotFound() {
        User user = userService.registerUser(new User(null, "Ana", "ana@eci.edu.co"));
        assertThrows(IllegalArgumentException.class,
                () -> loanService.loanBook("no-book", user.getId()));
    }

    @Test
    void loanBook_ShouldThrowException_WhenUserNotFound() {
        Book book = bookService.addBook(new Book(null, "Domain-Driven Design", "Eric Evans", "9780321125217"));
        assertThrows(IllegalArgumentException.class,
                () -> loanService.loanBook(book.getId(), "no-user"));
    }

    @Test
    void returnBook_ShouldMarkLoanAsReturned() {
        Book book = bookService.addBook(new Book(null, "TDD by Example", "Kent Beck", "9780321146533"));
        User user = userService.registerUser(new User(null, "Pedro", "pedro@eci.edu.co"));
        Loan loan = loanService.loanBook(book.getId(), user.getId());

        Loan returned = loanService.returnBook(loan.getId());

        assertTrue(returned.isReturned());
        assertTrue(returned.getBook().isAvailable());
    }

    @Test
    void returnBook_ShouldThrowException_WhenAlreadyReturned() {
        Book book = bookService.addBook(new Book(null, "SICP", "Abelson", "9780262510875"));
        User user = userService.registerUser(new User(null, "Laura", "laura@eci.edu.co"));
        Loan loan = loanService.loanBook(book.getId(), user.getId());
        loanService.returnBook(loan.getId());

        assertThrows(IllegalArgumentException.class,
                () -> loanService.returnBook(loan.getId()));
    }

    @Test
    void getAllLoans_ShouldReturnAllLoans() {
        Book book1 = bookService.addBook(new Book(null, "Book 1", "Author 1", "9780000000001"));
        Book book2 = bookService.addBook(new Book(null, "Book 2", "Author 2", "9780000000002"));
        User user = userService.registerUser(new User(null, "User", "user@eci.edu.co"));

        loanService.loanBook(book1.getId(), user.getId());
        loanService.loanBook(book2.getId(), user.getId());

        List<Loan> loans = loanService.getAllLoans();
        assertEquals(2, loans.size());
    }
}
