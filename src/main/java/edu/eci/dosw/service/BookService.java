package edu.eci.dosw.service;

import edu.eci.dosw.model.Book;
import edu.eci.dosw.persistence.entity.BookEntity;
import edu.eci.dosw.persistence.mapper.BookEntityMapper;
import edu.eci.dosw.persistence.repository.BookRepository;
import edu.eci.dosw.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar el catálogo de libros.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
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

        BookEntity entity = BookEntityMapper.toEntity(book);
        BookEntity saved = bookRepository.save(entity);
        return BookEntityMapper.toDomain(saved);
    }

    /**
     * Retorna todos los libros del catálogo.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Busca un libro por su ID.
     */
    public Optional<Book> findBookById(String id) {
        return bookRepository.findById(id)
                .map(BookEntityMapper::toDomain);
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
        BookEntity entity = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + bookId));

        int available = entity.getAvailableCopies() != null ? entity.getAvailableCopies() : 0;
        if (available <= 0) {
            throw new IllegalArgumentException("El libro no tiene ejemplares disponibles.");
        }

        entity.setAvailableCopies(available - 1);
        entity.setLoanedCopies((entity.getLoanedCopies() != null ? entity.getLoanedCopies() : 0) + 1);
        entity.setAvailable(entity.getAvailableCopies() > 0);
        entity.setStatus(entity.getAvailableCopies() > 0 ? "Disponible" : "Agotado");
        bookRepository.save(entity);
    }

    /**
     * Aumenta en uno la cantidad de ejemplares disponibles.
     */
    public void incrementAvailableCopies(String bookId) {
        BookEntity entity = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + bookId));

        int available = entity.getAvailableCopies() != null ? entity.getAvailableCopies() : 0;
        int total = entity.getTotalCopies() != null ? entity.getTotalCopies() : 0;

        if (available >= total) {
            throw new IllegalArgumentException("No se puede exceder el stock total del libro.");
        }

        entity.setAvailableCopies(available + 1);
        entity.setLoanedCopies((entity.getLoanedCopies() != null ? entity.getLoanedCopies() : 0) - 1);
        entity.setAvailable(true);
        entity.setStatus("Disponible");
        bookRepository.save(entity);
    }

    /**
     * Actualiza un libro existente.
     */
    public Book updateBook(String id, Book book) {
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + id));

        if (book.getTitle() != null) entity.setTitle(book.getTitle());
        if (book.getAuthor() != null) entity.setAuthor(book.getAuthor());
        if (book.getIsbn() != null) entity.setIsbn(book.getIsbn());
        if (book.getCategories() != null) entity.setCategories(book.getCategories());
        if (book.getPublicationType() != null) entity.setPublicationType(book.getPublicationType());
        if (book.getPublicationDate() != null) entity.setPublicationDate(book.getPublicationDate());

        if (book.getMetadata() != null) {
            entity.setPages(book.getMetadata().getPages());
            entity.setLanguage(book.getMetadata().getLanguage());
            entity.setPublisher(book.getMetadata().getPublisher());
        }

        if (book.getAvailability() != null) {
            if (book.getAvailability().getTotalCopies() != null) {
                entity.setTotalCopies(book.getAvailability().getTotalCopies());
            }
            if (book.getAvailability().getAvailableCopies() != null) {
                entity.setAvailableCopies(book.getAvailability().getAvailableCopies());
            }
            if (book.getAvailability().getLoanedCopies() != null) {
                entity.setLoanedCopies(book.getAvailability().getLoanedCopies());
            }
            if (book.getAvailability().getStatus() != null) {
                entity.setStatus(book.getAvailability().getStatus());
            }
        }

        BookEntity saved = bookRepository.save(entity);
        return BookEntityMapper.toDomain(saved);
    }
}
