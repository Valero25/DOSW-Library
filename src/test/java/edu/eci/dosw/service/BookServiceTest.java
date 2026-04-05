package edu.eci.dosw.service;

import edu.eci.dosw.model.Book;
import edu.eci.dosw.persistence.BookPersistenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookPersistenceRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book("book-1", "Clean Code", "Robert C. Martin", "9780132350884");
    }

    @Test
    void addBook_ShouldAddBookSuccessfully() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book input = new Book(null, "Clean Code", "Robert C. Martin", "9780132350884");
        Book result = bookService.addBook(input);

        assertNotNull(result.getId());
        assertEquals("Clean Code", result.getTitle());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void addBook_ShouldThrowException_WhenTitleIsBlank() {
        Book input = new Book(null, "", "Author", "9780132350884");
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(input));
    }

    @Test
    void addBook_ShouldThrowException_WhenBookIsNull() {
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(null));
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() {
        Book book2 = new Book("book-2", "Book 2", "Author 2", "ISBN2");

        when(bookRepository.findAll()).thenReturn(List.of(book, book2));

        List<Book> books = bookService.getAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    void findBookById_ShouldReturnBook_WhenExists() {
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));

        Optional<Book> found = bookService.findBookById("book-1");

        assertTrue(found.isPresent());
        assertEquals("Clean Code", found.get().getTitle());
    }

    @Test
    void findBookById_ShouldReturnEmpty_WhenNotExists() {
        when(bookRepository.findById("non-existent")).thenReturn(Optional.empty());

        Optional<Book> found = bookService.findBookById("non-existent");
        assertTrue(found.isEmpty());
    }

    @Test
    void removeBook_ShouldReturnTrue_WhenBookExists() {
        when(bookRepository.existsById("book-1")).thenReturn(true);

        assertTrue(bookService.removeBook("book-1"));
        verify(bookRepository).deleteById("book-1");
    }

    @Test
    void removeBook_ShouldReturnFalse_WhenBookNotFound() {
        when(bookRepository.existsById("fake-id")).thenReturn(false);

        assertFalse(bookService.removeBook("fake-id"));
        verify(bookRepository, never()).deleteById(any());
    }
}
