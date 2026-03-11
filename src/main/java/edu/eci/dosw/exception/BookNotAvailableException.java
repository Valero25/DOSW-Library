package edu.eci.dosw.exception;

/**
 * Exception thrown when a requested book is not available for loan.
 */
public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(String bookId) {
        super("Book with id '" + bookId + "' is not available for loan.");
    }

    public BookNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
