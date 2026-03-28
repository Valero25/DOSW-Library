package edu.eci.dosw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a book loan in the library system.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Loan {

    private String id;
    private Book book;
    private User user;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private boolean returned;
    
    private List<LoanHistory> loanHistory;

    public Loan(String id, Book book, User user, LocalDate loanDate) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.returned = false;
    }
}
