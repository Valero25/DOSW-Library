package edu.eci.dosw.model;

import java.time.LocalDate;

/**
 * Represents a book loan in the library system.
 */
public class Loan {

    private String id;
    private Book book;
    private User user;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private boolean returned;

    public Loan() {
    }

    public Loan(String id, Book book, User user, LocalDate loanDate) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.returned = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return "Loan{id='" + id + "', book=" + book + ", user=" + user
                + ", loanDate=" + loanDate + ", returned=" + returned + "}";
    }
}
