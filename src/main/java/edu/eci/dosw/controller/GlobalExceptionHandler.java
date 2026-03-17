package edu.eci.dosw.controller;

import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.exception.LoanLimitExceededException;
import edu.eci.dosw.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la API REST.
 * Intercepta las excepciones lanzadas por los controladores y servicios
 * para retornar respuestas HTTP estandarizadas en formato JSON.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Construye la respuesta JSON estandarizada con el error y marca de tiempo.
     */
    private Map<String, Object> buildBody(String error) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("error", error);
        return body;
    }

    /**
     * Maneja errores de validación de argumentos o estado ilegal.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildBody(ex.getMessage()));
    }

    /**
     * Maneja errores cuando un libro no tiene ejemplares disponibles.
     */
    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<Object> handleBookNotAvailableException(BookNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildBody(ex.getMessage()));
    }

    /**
     * Maneja errores cuando un usuario no existe en el sistema.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildBody(ex.getMessage()));
    }

    /**
     * Maneja errores cuando un usuario supera el límite de préstamos permitidos.
     */
    @ExceptionHandler(LoanLimitExceededException.class)
    public ResponseEntity<Object> handleLoanLimitExceededException(LoanLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(buildBody(ex.getMessage()));
    }

    /**
     * Maneja cualquier otra excepción no capturada explícitamente (Fallback).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(buildBody("Error interno del servidor: " + ex.getMessage()));
    }
}
