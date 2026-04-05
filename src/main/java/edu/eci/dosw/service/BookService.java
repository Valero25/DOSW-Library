package edu.eci.dosw.service;

import edu.eci.dosw.model.Availability;
import edu.eci.dosw.model.Book;
import edu.eci.dosw.persistence.BookPersistenceRepository;
import edu.eci.dosw.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar el catálogo de libros.
 */
@Service
public class BookService {

    private final BookPersistenceRepository bookRepository;

    public BookService(BookPersistenceRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Agrega un nuevo libro al catálogo.
     */
    public Book addBook(Book book) {
        ValidationUtil.validateNotNull(book, "book");
        ValidationUtil.validateNotBlank(book.getTitle(), "title");
        ValidationUtil.validateNotBlank(book.getAuthor(), "author");

        if (book.getAvailability() != null) {
            if (book.getAvailability().getTotalCopies() != null && book.getAvailability().getTotalCopies() <= 0) {
                throw new IllegalArgumentException("La cantidad de ejemplares totales debe ser mayor que 0.");
            }
            if (book.getAvailability().getAvailableCopies() != null && book.getAvailability().getAvailableCopies() < 0) {
                throw new IllegalArgumentException("La cantidad de ejemplares disponibles no puede ser menor a 0.");
            }
        }

        book.setAvailable(true);
        if (book.getDateAddedToCatalog() == null) {
            book.setDateAddedToCatalog(LocalDate.now());
        }

        return bookRepository.save(book);
    }

    /**
     * Retorna todos los libros del catálogo.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Busca un libro por su ID.
     */
    public Optional<Book> findBookById(String id) {
        return bookRepository.findById(id);
    }

    /**
     * Elimina un libro del catálogo.
     */
    public boolean removeBook(String id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Reduce en uno la cantidad de ejemplares disponibles.
     */
    public void decrementAvailableCopies(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + bookId));

        Availability avail = book.getAvailability();
        int available = (avail != null && avail.getAvailableCopies() != null) ? avail.getAvailableCopies() : 0;
        if (available <= 0) {
            throw new IllegalArgumentException("El libro no tiene ejemplares disponibles.");
        }

        if (avail == null) {
            avail = new Availability();
            book.setAvailability(avail);
        }
        avail.setAvailableCopies(available - 1);
        int loaned = avail.getLoanedCopies() != null ? avail.getLoanedCopies() : 0;
        avail.setLoanedCopies(loaned + 1);
        book.setAvailable(avail.getAvailableCopies() > 0);
        avail.setStatus(avail.getAvailableCopies() > 0 ? "Disponible" : "Agotado");
        bookRepository.save(book);
    }

    /**
     * Aumenta en uno la cantidad de ejemplares disponibles.
     */
    public void incrementAvailableCopies(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + bookId));

        Availability avail = book.getAvailability();
        int available = (avail != null && avail.getAvailableCopies() != null) ? avail.getAvailableCopies() : 0;
        int total = (avail != null && avail.getTotalCopies() != null) ? avail.getTotalCopies() : 0;

        if (available >= total) {
            throw new IllegalArgumentException("No se puede exceder el stock total del libro.");
        }

        if (avail == null) {
            avail = new Availability();
            book.setAvailability(avail);
        }
        avail.setAvailableCopies(available + 1);
        int loaned = avail.getLoanedCopies() != null ? avail.getLoanedCopies() : 0;
        avail.setLoanedCopies(loaned - 1);
        book.setAvailable(true);
        avail.setStatus("Disponible");
        bookRepository.save(book);
    }

    /**
     * Actualiza un libro existente.
     */
    public Book updateBook(String id, Book book) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + id));

        if (book.getTitle() != null) existing.setTitle(book.getTitle());
        if (book.getAuthor() != null) existing.setAuthor(book.getAuthor());
        if (book.getIsbn() != null) existing.setIsbn(book.getIsbn());
        if (book.getCategories() != null) existing.setCategories(book.getCategories());
        if (book.getPublicationType() != null) existing.setPublicationType(book.getPublicationType());
        if (book.getPublicationDate() != null) existing.setPublicationDate(book.getPublicationDate());

        if (book.getMetadata() != null) {
            existing.setMetadata(book.getMetadata());
        }

        if (book.getAvailability() != null) {
            Availability existAvail = existing.getAvailability();
            if (existAvail == null) {
                existing.setAvailability(book.getAvailability());
            } else {
                Availability newAvail = book.getAvailability();
                if (newAvail.getTotalCopies() != null) existAvail.setTotalCopies(newAvail.getTotalCopies());
                if (newAvail.getAvailableCopies() != null) existAvail.setAvailableCopies(newAvail.getAvailableCopies());
                if (newAvail.getLoanedCopies() != null) existAvail.setLoanedCopies(newAvail.getLoanedCopies());
                if (newAvail.getStatus() != null) existAvail.setStatus(newAvail.getStatus());
            }
        }

        return bookRepository.save(existing);
    }
}
