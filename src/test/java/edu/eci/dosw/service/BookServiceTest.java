package edu.eci.dosw.service;

import edu.eci.dosw.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
    }

    @Test
    void addBook_ShouldAddBookSuccessfully() {
        Book book = new Book(null, "Clean Code", "Robert C. Martin", "9780132350884");
        Book result = bookService.addBook(book);

        assertNotNull(result.getId());
        assertEquals("Clean Code", result.getTitle());
        assertTrue(result.isAvailable());
    }

    @Test
    void addBook_ShouldThrowException_WhenTitleIsBlank() {
        Book book = new Book(null, "", "Author", "9780132350884");
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book));
    }

    @Test
    void addBook_ShouldThrowException_WhenBookIsNull() {
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(null));
    }

    @Test
    void getAllBooks_ShouldReturnAllAddedBooks() {
        bookService.addBook(new Book(null, "Book 1", "Author 1", "9780000000001"));
        bookService.addBook(new Book(null, "Book 2", "Author 2", "9780000000002"));

        List<Book> books = bookService.getAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    void findBookById_ShouldReturnBook_WhenExists() {
        Book book = bookService.addBook(new Book(null, "DDD", "Eric Evans", "9780321125217"));
        Optional<Book> found = bookService.findBookById(book.getId());

        assertTrue(found.isPresent());
        assertEquals("DDD", found.get().getTitle());
    }

    @Test
    void findBookById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Book> found = bookService.findBookById("non-existent-id");
        assertTrue(found.isEmpty());
    }

    @Test
    void removeBook_ShouldReturnTrue_WhenBookExists() {
        Book book = bookService.addBook(new Book(null, "Test Book", "Author", "9780000000003"));
        assertTrue(bookService.removeBook(book.getId()));
        assertTrue(bookService.getAllBooks().isEmpty());
    }

    @Test
    void removeBook_ShouldReturnFalse_WhenBookNotFound() {
        assertFalse(bookService.removeBook("fake-id"));
    }
}
