package edu.eci.dosw.service;

import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.model.Availability;
import edu.eci.dosw.model.Book;
import edu.eci.dosw.model.Loan;
import edu.eci.dosw.model.User;
import edu.eci.dosw.persistence.BookPersistenceRepository;
import edu.eci.dosw.persistence.LoanPersistenceRepository;
import edu.eci.dosw.persistence.UserPersistenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanPersistenceRepository loanRepository;

    @Mock
    private BookPersistenceRepository bookRepository;

    @Mock
    private UserPersistenceRepository userRepository;

    @InjectMocks
    private LoanService loanService;

    private Book book;
    private User user;

    @BeforeEach
    void setUp() {
        book = new Book("book-1", "Clean Architecture", "Robert C. Martin", "ISBN1");
        Availability availability = new Availability("Disponible", 5, 5, 0);
        book.setAvailability(availability);
        book.setAvailable(true);

        user = new User();
        user.setId("user-1");
        user.setName("Maria");
        user.setEmail("maria@eci.edu.co");
        user.setUsername("maria");
        user.setPassword("encoded");
        user.setRole("USER");
    }

    @Test
    void loanBook_ShouldCreateLoan_WhenBookAvailable() {
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

        Loan savedLoan = new Loan();
        savedLoan.setId("loan-1");
        savedLoan.setBook(book);
        savedLoan.setUser(user);
        savedLoan.setLoanDate(LocalDate.now());
        savedLoan.setReturned(false);
        savedLoan.setLoanHistory(new ArrayList<>());

        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        Loan loan = loanService.loanBook("book-1", "user-1");

        assertNotNull(loan.getId());
        verify(bookRepository).save(any(Book.class));
        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void loanBook_ShouldThrowException_WhenNoAvailableCopies() {
        book.getAvailability().setAvailableCopies(0);
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

        assertThrows(BookNotAvailableException.class,
                () -> loanService.loanBook("book-1", "user-1"));
    }

    @Test
    void loanBook_ShouldThrowException_WhenBookNotFound() {
        when(bookRepository.findById("no-book")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> loanService.loanBook("no-book", "user-1"));
    }

    @Test
    void loanBook_ShouldThrowException_WhenUserNotFound() {
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(book));
        when(userRepository.findById("no-user")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> loanService.loanBook("book-1", "no-user"));
    }

    @Test
    void returnBook_ShouldMarkLoanAsReturned() {
        Loan loan = new Loan();
        loan.setId("loan-1");
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setReturned(false);
        loan.setLoanHistory(new ArrayList<>());
        book.getAvailability().setAvailableCopies(4);
        book.getAvailability().setLoanedCopies(1);

        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan returned = loanService.returnBook("loan-1");

        assertTrue(returned.isReturned());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void returnBook_ShouldThrowException_WhenAlreadyReturned() {
        Loan loan = new Loan();
        loan.setId("loan-1");
        loan.setReturned(true);

        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));

        assertThrows(IllegalArgumentException.class,
                () -> loanService.returnBook("loan-1"));
    }

    @Test
    void returnBook_ShouldThrowException_WhenLoanNotFound() {
        when(loanRepository.findById("fake")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> loanService.returnBook("fake"));
    }

    @Test
    void getAllLoans_ShouldReturnAllLoans() {
        Loan loan1 = new Loan();
        loan1.setId("loan-1");
        loan1.setBook(book);
        loan1.setUser(user);
        loan1.setLoanDate(LocalDate.now());
        loan1.setLoanHistory(new ArrayList<>());

        when(loanRepository.findAll()).thenReturn(List.of(loan1));

        List<Loan> loans = loanService.getAllLoans();
        assertEquals(1, loans.size());
    }
}
