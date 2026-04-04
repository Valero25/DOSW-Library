package edu.eci.dosw.controller;

import edu.eci.dosw.controller.dto.LoanDTO;
import edu.eci.dosw.model.Book;
import edu.eci.dosw.model.Loan;
import edu.eci.dosw.model.User;
import edu.eci.dosw.model.enums.MembershipType;
import edu.eci.dosw.security.JwtService;
import edu.eci.dosw.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests para el controlador de préstamos (LoanController).
 * Verifica que ambos USER y LIBRARIAN pueden acceder según protecciones.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "jwt.secret=test-secret-key-2026-must-be-at-least-256-bits-long-for-hs256",
        "jwt.expiration=3600000"
})
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Autowired
    private JwtService jwtService;

    private String librarianToken;
    private String userToken;
    private LoanDTO loanDTO;

    @BeforeEach
    void setUp() {
        // Generate tokens
        librarianToken = jwtService.generateToken("librarian", "LIBRARIAN", "librarian-1");
        userToken = jwtService.generateToken("regularuser", "USER", "user-1");

        // Setup test loan DTO
        loanDTO = new LoanDTO();
        loanDTO.setId("1");
        loanDTO.setUserId("user-1");
        loanDTO.setBookId("book-1");
    }

    // ===== GET Requests (Authenticated) =====

    @Test
    void getAllLoans_WithValidUserToken_ShouldReturnLoans() throws Exception {
        Book mockBook = new Book("1", "Test Book", "Author", "ISBN");
        User mockUser = new User("1", "Test User", "test@example.com", "testuser", null, "USER", null, LocalDate.now());
        Loan loan = new Loan("1", mockBook, mockUser, LocalDate.now());
        
        when(loanService.getAllLoans()).thenReturn(List.of(loan));

        mockMvc.perform(get("/api/loans")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(loanService).getAllLoans();
    }

    @Test
    void getAllLoans_WithLIBRARIANToken_ShouldReturnLoans() throws Exception {
        Book mockBook = new Book("1", "Test Book", "Author", "ISBN");
        User mockUser = new User("1", "Test User", "test@example.com", "testuser", null, "USER", null, LocalDate.now());
        Loan loan = new Loan("1", mockBook, mockUser, LocalDate.now());
        
        when(loanService.getAllLoans()).thenReturn(List.of(loan));

        mockMvc.perform(get("/api/loans")
                .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(loanService).getAllLoans();
    }

    @Test
    void getAllLoans_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isForbidden());

        verify(loanService, never()).getAllLoans();
    }

    // ===== POST Requests (Authenticated - both USER and LIBRARIAN) =====

    @Test
    void loanBook_WithUserToken_ShouldCreateLoan() throws Exception {
        Book mockBook = new Book("1", "Test Book", "Author", "ISBN");
        User mockUser = new User("1", "Test User", "test@example.com", "testuser", null, "USER", null, LocalDate.now());
        Loan loan = new Loan("1", mockBook, mockUser, LocalDate.now());
        
        when(loanService.loanBook(anyString(), anyString())).thenReturn(loan);

        mockMvc.perform(post("/api/loans")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": \"user-1\", \"bookId\": \"book-1\"}")
        )
                .andExpect(status().isCreated());

        verify(loanService).loanBook(anyString(), anyString());
    }

    @Test
    void loanBook_WithLIBRARIANToken_ShouldCreateLoan() throws Exception {
        Book mockBook = new Book("1", "Test Book", "Author", "ISBN");
        User mockUser = new User("1", "Test User", "test@example.com", "testuser", null, "USER", null, LocalDate.now());
        Loan loan = new Loan("1", mockBook, mockUser, LocalDate.now());
        
        when(loanService.loanBook(anyString(), anyString())).thenReturn(loan);

        mockMvc.perform(post("/api/loans")
                .header("Authorization", "Bearer " + librarianToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": \"user-1\", \"bookId\": \"book-1\"}")
        )
                .andExpect(status().isCreated());

        verify(loanService).loanBook(anyString(), anyString());
    }

    @Test
    void loanBook_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": \"user-1\", \"bookId\": \"book-1\"}")
        )
                .andExpect(status().isForbidden());

        verify(loanService, never()).loanBook(anyString(), anyString());
    }

    // ===== PUT Requests (Authenticated - Return book) =====

    @Test
    void returnBook_WithUserToken_ShouldReturnLoan() throws Exception {
        Book mockBook = new Book("1", "Test Book", "Author", "ISBN");
        User mockUser = new User("1", "Test User", "test@example.com", "testuser", null, "USER", null, LocalDate.now());
        Loan loan = new Loan("1", mockBook, mockUser, LocalDate.now());
        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());
        
        when(loanService.returnBook(anyString())).thenReturn(loan);

        mockMvc.perform(put("/api/loans/1/return")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        verify(loanService).returnBook(anyString());
    }

    @Test
    void returnBook_WithLIBRARIANToken_ShouldReturnLoan() throws Exception {
        Book mockBook = new Book("1", "Test Book", "Author", "ISBN");
        User mockUser = new User("1", "Test User", "test@example.com", "testuser", null, "USER", null, LocalDate.now());
        Loan loan = new Loan("1", mockBook, mockUser, LocalDate.now());
        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());
        
        when(loanService.returnBook(anyString())).thenReturn(loan);

        mockMvc.perform(put("/api/loans/1/return")
                .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isOk());

        verify(loanService).returnBook(anyString());
    }

    @Test
    void returnBook_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(put("/api/loans/1/return"))
                .andExpect(status().isForbidden());

        verify(loanService, never()).returnBook(anyString());
    }
}
