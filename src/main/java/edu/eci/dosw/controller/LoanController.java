package edu.eci.dosw.controller;

import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.model.Loan;
import edu.eci.dosw.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for loan-related operations.
 */
@RestController
@RequestMapping("/api/loans")
@Tag(name = "Préstamos", description = "Operaciones de préstamo y devolución de libros")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Operation(summary = "Obtener todos los préstamos", description = "Retorna la lista de todos los préstamos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de préstamos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @Operation(summary = "Crear un préstamo", description = "Registra un nuevo préstamo de libro para un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Préstamo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Libro no disponible para préstamo")
    })
    @PostMapping
    public ResponseEntity<?> loanBook(@RequestBody Map<String, String> body) {
        String bookId = body.get("bookId");
        String userId = body.get("userId");
        try {
            Loan loan = loanService.loanBook(bookId, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(loan);
        } catch (BookNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Devolver un libro", description = "Completa un préstamo registrando la devolución del libro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro devuelto exitosamente"),
            @ApiResponse(responseCode = "400", description = "Préstamo ya devuelto o no encontrado")
    })
    @PutMapping("/{loanId}/return")
    public ResponseEntity<?> returnBook(
            @Parameter(description = "ID del préstamo a completar") @PathVariable String loanId) {
        try {
            Loan loan = loanService.returnBook(loanId);
            return ResponseEntity.ok(loan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
