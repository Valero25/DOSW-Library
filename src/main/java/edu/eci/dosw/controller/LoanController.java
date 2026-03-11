package edu.eci.dosw.controller;

import edu.eci.dosw.exception.BookNotAvailableException;
import edu.eci.dosw.model.Loan;
import edu.eci.dosw.service.LibraryService;
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
public class LoanController {

    private final LibraryService libraryService;

    public LoanController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * Retrieves all loans in the system.
     *
     * @return list of all loans
     */
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(libraryService.getAllLoans());
    }

    /**
     * Creates a new loan for a book.
     * Expects a JSON body: { "bookId": "...", "userId": "..." }
     *
     * @param body map containing bookId and userId
     * @return the created loan, or an error response
     */
    @PostMapping
    public ResponseEntity<?> loanBook(@RequestBody Map<String, String> body) {
        String bookId = body.get("bookId");
        String userId = body.get("userId");
        try {
            Loan loan = libraryService.loanBook(bookId, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(loan);
        } catch (BookNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Returns a book by completing the specified loan.
     *
     * @param loanId the ID of the loan to complete
     * @return the updated loan
     */
    @PutMapping("/{loanId}/return")
    public ResponseEntity<?> returnBook(@PathVariable String loanId) {
        try {
            Loan loan = libraryService.returnBook(loanId);
            return ResponseEntity.ok(loan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
