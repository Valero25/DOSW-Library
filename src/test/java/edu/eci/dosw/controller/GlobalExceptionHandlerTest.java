package edu.eci.dosw.controller;

import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.exception.LoanLimitExceededException;
import edu.eci.dosw.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleIllegalArgument() {
        ResponseEntity<Object> res = handler.handleIllegalArgumentException(new IllegalArgumentException("error"));
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        ;
        assertTrue(res.getBody().toString().contains("error"));
    }

    @Test
    void testHandleBookNotAvailable() {
        ResponseEntity<Object> res = handler.handleBookNotAvailableException(new BookNotAvailableException("b1"));
        assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    void testHandleUserNotFound() {
        ResponseEntity<Object> res = handler.handleUserNotFoundException(new UserNotFoundException("u1"));
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    void testHandleLoanLimitExceeded() {
        ResponseEntity<Object> res = handler.handleLoanLimitExceededException(new LoanLimitExceededException("u1"));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, res.getStatusCode());
    }

    @Test
    void testHandleGeneralException() {
        ResponseEntity<Object> res = handler.handleGeneralException(new Exception("gen"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
    }
}
