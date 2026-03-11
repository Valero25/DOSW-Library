package edu.eci.dosw.service;

import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.model.Book;
import edu.eci.dosw.model.Loan;
import edu.eci.dosw.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {

    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        libraryService = new LibraryService();
    }

    // ----------------------- Book Tests -----------------------

    @Test
    void addBook_ShouldAddBookSuccessfully() {
        Book book = new Book(null, "Clean Code", "Robert C. Martin", "9780132350884");
        Book result = libraryService.addBook(book);

        assertNotNull(result.getId());
        assertEquals("Clean Code", result.getTitle());
        assertTrue(result.isAvailable());
    }

    @Test
    void addBook_ShouldThrowException_WhenTitleIsBlank() {
        Book book = new Book(null, "", "Author", "9780132350884");
        assertThrows(IllegalArgumentException.class, () -> libraryService.addBook(book));
    }

    @Test
    void addBook_ShouldThrowException_WhenBookIsNull() {
        assertThrows(IllegalArgumentException.class, () -> libraryService.addBook(null));
    }

    @Test
    void getAllBooks_ShouldReturnAllAddedBooks() {
        libraryService.addBook(new Book(null, "Book 1", "Author 1", "9780000000001"));
        libraryService.addBook(new Book(null, "Book 2", "Author 2", "9780000000002"));

        List<Book> books = libraryService.getAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    void findBookById_ShouldReturnBook_WhenExists() {
        Book book = libraryService.addBook(new Book(null, "DDD", "Eric Evans", "9780321125217"));
        Optional<Book> found = libraryService.findBookById(book.getId());

        assertTrue(found.isPresent());
        assertEquals("DDD", found.get().getTitle());
    }

    @Test
    void findBookById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Book> found = libraryService.findBookById("non-existent-id");
        assertFalse(found.isPresent());
    }

    @Test
    void removeBook_ShouldReturnTrue_WhenBookExists() {
        Book book = libraryService.addBook(new Book(null, "Test Book", "Author", "9780000000003"));
        assertTrue(libraryService.removeBook(book.getId()));
        assertTrue(libraryService.getAllBooks().isEmpty());
    }

    @Test
    void removeBook_ShouldReturnFalse_WhenBookNotFound() {
        assertFalse(libraryService.removeBook("fake-id"));
    }

    // ----------------------- User Tests -----------------------

    @Test
    void registerUser_ShouldRegisterSuccessfully() {
        User user = new User(null, "Juan", "juan@eci.edu.co");
        User result = libraryService.registerUser(user);

        assertNotNull(result.getId());
        assertEquals("Juan", result.getName());
    }

    @Test
    void registerUser_ShouldThrowException_WhenNameIsBlank() {
        User user = new User(null, " ", "juan@eci.edu.co");
        assertThrows(IllegalArgumentException.class, () -> libraryService.registerUser(user));
    }

    // ----------------------- Loan Tests -----------------------

    @Test
    void loanBook_ShouldCreateLoan_WhenBookAndUserExist() {
        Book book = libraryService.addBook(new Book(null, "Clean Architecture", "Robert C. Martin", "9780134494166"));
        User user = libraryService.registerUser(new User(null, "Maria", "maria@eci.edu.co"));

        Loan loan = libraryService.loanBook(book.getId(), user.getId());

        assertNotNull(loan.getId());
        assertFalse(loan.getBook().isAvailable());
    }

    @Test
    void loanBook_ShouldThrowBookNotAvailableException_WhenAlreadyLoaned() {
        Book book = libraryService.addBook(new Book(null, "Refactoring", "Martin Fowler", "9780201485677"));
        User user = libraryService.registerUser(new User(null, "Carlos", "carlos@eci.edu.co"));

        libraryService.loanBook(book.getId(), user.getId());

        assertThrows(BookNotAvailableException.class,
                () -> libraryService.loanBook(book.getId(), user.getId()));
    }

    @Test
    void loanBook_ShouldThrowException_WhenBookNotFound() {
        User user = libraryService.registerUser(new User(null, "Ana", "ana@eci.edu.co"));
        assertThrows(IllegalArgumentException.class,
                () -> libraryService.loanBook("no-book", user.getId()));
    }

    @Test
    void returnBook_ShouldMarkLoanAsReturned() {
        Book book = libraryService.addBook(new Book(null, "TDD by Example", "Kent Beck", "9780321146533"));
        User user = libraryService.registerUser(new User(null, "Pedro", "pedro@eci.edu.co"));
        Loan loan = libraryService.loanBook(book.getId(), user.getId());

        Loan returned = libraryService.returnBook(loan.getId());

        assertTrue(returned.isReturned());
        assertTrue(returned.getBook().isAvailable());
    }

    @Test
    void returnBook_ShouldThrowException_WhenAlreadyReturned() {
        Book book = libraryService.addBook(new Book(null, "SICP", "Abelson", "9780262510875"));
        User user = libraryService.registerUser(new User(null, "Laura", "laura@eci.edu.co"));
        Loan loan = libraryService.loanBook(book.getId(), user.getId());
        libraryService.returnBook(loan.getId());

        assertThrows(IllegalArgumentException.class,
                () -> libraryService.returnBook(loan.getId()));
    }
}
