package edu.eci.dosw.controller;

import edu.eci.dosw.model.Book;
import edu.eci.dosw.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for book-related operations.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves all books in the catalog.
     *
     * @return list of all books
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    /**
     * Retrieves a single book by its ID.
     *
     * @param id the book ID
     * @return the book if found, 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        return bookService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Adds a new book to the catalog.
     *
     * @param book the book to add
     * @return the created book
     */
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book created = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Removes a book from the catalog.
     *
     * @param id the ID of the book to remove
     * @return 204 if removed, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeBook(@PathVariable String id) {
        boolean removed = bookService.removeBook(id);
        return removed ? ResponseEntity.noContent().build()
                       : ResponseEntity.notFound().build();
    }
}
