package edu.eci.dosw.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * Representa un préstamo de libro en el sistema de la biblioteca.
 * Asocia un libro con un usuario durante un período determinado y registra su estado de devolución.
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
