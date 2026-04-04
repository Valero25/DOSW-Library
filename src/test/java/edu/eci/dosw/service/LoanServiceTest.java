package edu.eci.dosw.service;

import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.model.Loan;
import edu.eci.dosw.persistence.entity.BookEntity;
import edu.eci.dosw.persistence.entity.LoanEntity;
import edu.eci.dosw.persistence.entity.UserEntity;
import edu.eci.dosw.persistence.repository.BookRepository;
import edu.eci.dosw.persistence.repository.LoanRepository;
import edu.eci.dosw.persistence.repository.UserRepository;
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
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanService loanService;

    private BookEntity bookEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        bookEntity = new BookEntity();
        bookEntity.setId("book-1");
        bookEntity.setTitle("Clean Architecture");
        bookEntity.setAuthor("Robert C. Martin");
        bookEntity.setAvailable(true);
        bookEntity.setTotalCopies(5);
        bookEntity.setAvailableCopies(5);
        bookEntity.setLoanedCopies(0);

        userEntity = new UserEntity();
        userEntity.setId("user-1");
        userEntity.setName("Maria");
        userEntity.setEmail("maria@eci.edu.co");
        userEntity.setUsername("maria");
        userEntity.setPassword("encoded");
        userEntity.setRole("USER");
    }

    @Test
    void loanBook_ShouldCreateLoan_WhenBookAvailable() {
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(bookEntity));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(userEntity));

        LoanEntity savedLoan = new LoanEntity();
        savedLoan.setId("loan-1");
        savedLoan.setBook(bookEntity);
        savedLoan.setUser(userEntity);
        savedLoan.setLoanDate(LocalDate.now());
        savedLoan.setReturned(false);
        savedLoan.setLoanHistory(new ArrayList<>());

        when(bookRepository.save(any(BookEntity.class))).thenReturn(bookEntity);
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(savedLoan);

        Loan loan = loanService.loanBook("book-1", "user-1");

        assertNotNull(loan.getId());
        verify(bookRepository).save(any(BookEntity.class));
        verify(loanRepository).save(any(LoanEntity.class));
    }

    @Test
    void loanBook_ShouldThrowException_WhenNoAvailableCopies() {
        bookEntity.setAvailableCopies(0);
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(bookEntity));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(userEntity));

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
        when(bookRepository.findById("book-1")).thenReturn(Optional.of(bookEntity));
        when(userRepository.findById("no-user")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> loanService.loanBook("book-1", "no-user"));
    }

    @Test
    void returnBook_ShouldMarkLoanAsReturned() {
        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setId("loan-1");
        loanEntity.setBook(bookEntity);
        loanEntity.setUser(userEntity);
        loanEntity.setLoanDate(LocalDate.now());
        loanEntity.setReturned(false);
        loanEntity.setLoanHistory(new ArrayList<>());
        bookEntity.setAvailableCopies(4);
        bookEntity.setLoanedCopies(1);

        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loanEntity));
        when(bookRepository.save(any(BookEntity.class))).thenReturn(bookEntity);
        when(loanRepository.save(any(LoanEntity.class))).thenReturn(loanEntity);

        Loan returned = loanService.returnBook("loan-1");

        assertTrue(returned.isReturned());
        verify(bookRepository).save(any(BookEntity.class));
    }

    @Test
    void returnBook_ShouldThrowException_WhenAlreadyReturned() {
        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setId("loan-1");
        loanEntity.setReturned(true);

        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loanEntity));

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
        LoanEntity loan1 = new LoanEntity();
        loan1.setId("loan-1");
        loan1.setBook(bookEntity);
        loan1.setUser(userEntity);
        loan1.setLoanDate(LocalDate.now());
        loan1.setLoanHistory(new ArrayList<>());

        when(loanRepository.findAll()).thenReturn(List.of(loan1));

        List<Loan> loans = loanService.getAllLoans();
        assertEquals(1, loans.size());
    }
}
