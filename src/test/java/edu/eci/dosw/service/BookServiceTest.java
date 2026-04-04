package edu.eci.dosw.service;

import edu.eci.dosw.model.Book;
import edu.eci.dosw.persistence.entity.BookEntity;
import edu.eci.dosw.persistence.repository.BookRepository;
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
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private BookEntity bookEntity;

    @BeforeEach
    void setUp() {
        bookEntity = new BookEntity();
        bookEntity.setId("book-1");
        bookEntity.setTitle("Clean Code");
        bookEntity.setAuthor("Robert C. Martin");
        bookEntity.setIsbn("9780132350884");
        bookEntity.setAvailable(true);
    }

    @Test
    void addBook_ShouldAddBookSuccessfully() {
        when(bookRepository.save(any(BookEntity.class))).thenReturn(bookEntity);

        Book book = new Book(null, "Clean Code", "Robert C. Martin", "9780132350884");
        Book result = bookService.addBook(book);

        assertNotNull(result.getId());
        assertEquals("Clean Code", result.getTitle());
        verify(bookRepository).save(any(BookEntity.class));
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
    void getAllBooks_ShouldReturnAllBooks() {
        BookEntity entity2 = new BookEntity();
        entity2.setId("book-2");
        entity2.setTitle("Book 2");
        entity2.setAuthor("Author 2");

        when(bookRepository.findAll()).thenReturn(List.of(bookEntity, entity2));

        List<Book> books = bookService.getAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    void findBookById_ShouldReturnBook_WhenExists() {
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(bookEntity));

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
