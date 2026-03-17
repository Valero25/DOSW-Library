package edu.eci.dosw.service;

import edu.eci.dosw.model.Book;
import edu.eci.dosw.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class encapsulating book-related operations.
 */
@Service
public class BookService {

    private final List<Book> books = new ArrayList<>();

    /**
     * Adds a new book to the library catalog.
     *
     * @param book the book to add
     * @return the added book
     */
    public Book addBook(Book book) {
        ValidationUtil.validateNotNull(book, "book");
        ValidationUtil.validateNotBlank(book.getTitle(), "title");
        ValidationUtil.validateNotBlank(book.getAuthor(), "author");
        if (book.getId() == null || book.getId().isBlank()) {
            book.setId(UUID.randomUUID().toString());
        }
        books.add(book);
        return book;
    }

    /**
     * Returns all books in the catalog.
     *
     * @return list of all books
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Finds a book by its ID.
     *
     * @param id the book ID
     * @return Optional containing the book if found
     */
    public Optional<Book> findBookById(String id) {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    /**
     * Removes a book from the catalog.
     *
     * @param id the ID of the book to remove
     * @return true if removed, false if not found
     */
    public boolean removeBook(String id) {
        return books.removeIf(b -> b.getId().equals(id));
    }
}
