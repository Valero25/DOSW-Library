package edu.eci.dosw.controller;

import edu.eci.dosw.controller.dto.BookDTO;
import edu.eci.dosw.model.Book;
import edu.eci.dosw.security.JwtService;
import edu.eci.dosw.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests para el controlador de libros (BookController).
 * Verifica protección por JWT y autorización por rol (LIBRARIAN vs USER).
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "jwt.secret=test-secret-key-2026-must-be-at-least-256-bits-long-for-hs256",
        "jwt.expiration=3600000"
})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private JwtService jwtService;

    private String librarianToken;
    private String userToken;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        // Generate tokens
        librarianToken = jwtService.generateToken("librarian", "LIBRARIAN", "librarian-1");
        userToken = jwtService.generateToken("regularuser", "USER", "user-1");

        // Setup test book DTO
        bookDTO = new BookDTO();
        bookDTO.setId("1");
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setIsbn("123456789");
    }

    // ===== GET Requests (Authenticated but no role requirement) =====

    @Test
    void getAllBooks_WithValidUserToken_ShouldReturnBooks() throws Exception {
        Book book = new Book("1", "Test Book", "Test Author", "123456789");
        when(bookService.getAllBooks()).thenReturn(List.of(book));

        mockMvc.perform(get("/api/books")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(bookService).getAllBooks();
    }

    @Test
    void getAllBooks_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isForbidden());

        verify(bookService, never()).getAllBooks();
    }

    @Test
    void getBookById_WithValidUserToken_ShouldReturnBook() throws Exception {
        Book book = new Book("1", "Test Book", "Test Author", "123456789");
        when(bookService.findBookById("1")).thenReturn(java.util.Optional.of(book));

        mockMvc.perform(get("/api/books/1")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));

        verify(bookService).findBookById("1");
    }

    // ===== POST Requests (Only LIBRARIAN) =====

    @Test
    void addBook_WithLIBRARIANToken_ShouldCreateBook() throws Exception {
        Book book = new Book("1", "New Book", "Author", "ISBN123");
        when(bookService.addBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                .header("Authorization", "Bearer " + librarianToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"New Book\", \"author\": \"Author\", \"isbn\": \"ISBN123\"}")
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Book"));

        verify(bookService).addBook(any(Book.class));
    }

    @Test
    void addBook_WithUserToken_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/books")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"New Book\", \"author\": \"Author\", \"isbn\": \"ISBN123\"}")
        )
                .andExpect(status().isForbidden());

        verify(bookService, never()).addBook(any(Book.class));
    }

    @Test
    void addBook_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"New Book\", \"author\": \"Author\", \"isbn\": \"ISBN123\"}")
        )
                .andExpect(status().isForbidden());

        verify(bookService, never()).addBook(any(Book.class));
    }

    // ===== DELETE Requests (Only LIBRARIAN) =====

    @Test
    void deleteBook_WithLIBRARIANToken_ShouldDeleteBook() throws Exception {
        when(bookService.removeBook("1")).thenReturn(true);

        mockMvc.perform(delete("/api/books/1")
                .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isNoContent());

        verify(bookService).removeBook("1");
    }

    @Test
    void deleteBook_WithUserToken_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/api/books/1")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        verify(bookService, never()).removeBook("1");
    }

    @Test
    void deleteBook_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isForbidden());

        verify(bookService, never()).removeBook("1");
    }
}
