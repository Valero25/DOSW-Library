package edu.eci.dosw.core.service;

import edu.eci.dosw.core.model.Book;
import edu.eci.dosw.core.util.IdGeneratorUtil;
import edu.eci.dosw.core.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar el catálogo de libros de la biblioteca.
 * Administra los libros y la cantidad de ejemplares disponibles mediante un mapa interno.
 */
@Service("coreBookService")
public class BookService {

    /**
     * Mapa que asocia cada libro con su número de ejemplares disponibles.
     */
    private final Map<Book, Integer> bookCopies = new LinkedHashMap<>();

    /**
     * Agrega un nuevo libro al catálogo con el número de ejemplares indicado.
     * Si el libro no posee identificador, se genera uno automáticamente.
     *
     * @param book   libro a registrar en el catálogo
     * @param copies número de ejemplares iniciales; debe ser mayor que cero
     * @return el libro registrado con su identificador asignado
     * @throws IllegalArgumentException si el libro es nulo, el título o autor están vacíos,
     *                                  o el número de ejemplares es menor o igual a cero
     */
    public Book addBook(Book book, int copies) {
        ValidationUtil.validateNotNull(book, "libro");
        ValidationUtil.validateNotBlank(book.getTitle(), "título");
        ValidationUtil.validateNotBlank(book.getAuthor(), "autor");
        if (copies <= 0) {
            throw new IllegalArgumentException("El número de ejemplares debe ser mayor que cero.");
        }
        if (book.getId() == null || book.getId().isBlank()) {
            book.setId(IdGeneratorUtil.generate());
        }
        book.setAvailable(true);
        bookCopies.put(book, copies);
        return book;
    }

    /**
     * Agrega ejemplares adicionales a un libro ya registrado en el catálogo.
     *
     * @param bookId identificador del libro al que se agregarán ejemplares
     * @param copies número de ejemplares a agregar; debe ser mayor que cero
     * @throws IllegalArgumentException si el libro no existe o el número de ejemplares es inválido
     */
    public void addCopies(String bookId, int copies) {
        if (copies <= 0) {
            throw new IllegalArgumentException("El número de ejemplares a agregar debe ser mayor que cero.");
        }
        Map.Entry<Book, Integer> entry = findEntryById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + bookId));
        bookCopies.put(entry.getKey(), entry.getValue() + copies);
        entry.getKey().setAvailable(true);
    }

    /**
     * Retorna todos los libros registrados en el catálogo.
     *
     * @return lista de todos los libros
     */
    public List<Book> getAllBooks() {
        return bookCopies.keySet().stream().collect(Collectors.toList());
    }

    /**
     * Busca un libro por su identificador único.
     *
     * @param id identificador del libro
     * @return un {@link Optional} con el libro si existe, o vacío si no se encuentra
     */
    public Optional<Book> findBookById(String id) {
        return bookCopies.keySet().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    /**
     * Consulta la cantidad de ejemplares disponibles de un libro.
     *
     * @param bookId identificador del libro
     * @return número de ejemplares disponibles
     * @throws IllegalArgumentException si el libro no está registrado en el catálogo
     */
    public int getAvailableCopies(String bookId) {
        return findEntryById(bookId)
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + bookId));
    }

    /**
     * Elimina un libro del catálogo.
     *
     * @param id identificador del libro a eliminar
     * @return {@code true} si el libro fue eliminado; {@code false} si no se encontró
     */
    public boolean removeBook(String id) {
        Optional<Map.Entry<Book, Integer>> entry = findEntryById(id);
        entry.ifPresent(e -> bookCopies.remove(e.getKey()));
        return entry.isPresent();
    }

    /**
     * Reduce en uno la cantidad de ejemplares disponibles del libro indicado.
     * Si los ejemplares llegan a cero, el libro se marca como no disponible.
     *
     * @param bookId identificador del libro
     * @throws IllegalArgumentException si el libro no existe o no tiene ejemplares disponibles
     */
    public void decrementCopies(String bookId) {
        Map.Entry<Book, Integer> entry = findEntryById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + bookId));
        int current = entry.getValue();
        if (current <= 0) {
            throw new IllegalArgumentException("El libro '" + bookId + "' no tiene ejemplares disponibles.");
        }
        int updated = current - 1;
        bookCopies.put(entry.getKey(), updated);
        if (updated == 0) {
            entry.getKey().setAvailable(false);
        }
    }

    /**
     * Aumenta en uno la cantidad de ejemplares disponibles del libro indicado.
     * El libro se marca como disponible al tener al menos un ejemplar.
     *
     * @param bookId identificador del libro
     * @throws IllegalArgumentException si el libro no está registrado en el catálogo
     */
    public void incrementCopies(String bookId) {
        Map.Entry<Book, Integer> entry = findEntryById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + bookId));
        bookCopies.put(entry.getKey(), entry.getValue() + 1);
        entry.getKey().setAvailable(true);
    }

    /**
     * Retorna el mapa completo de libros con sus ejemplares disponibles.
     *
     * @return mapa inmutable de libro → cantidad de ejemplares
     */
    public Map<Book, Integer> getAllBooksWithCopies() {
        return Map.copyOf(bookCopies);
    }

    /**
     * Busca la entrada del mapa correspondiente al identificador de libro indicado.
     *
     * @param bookId identificador del libro
     * @return un {@link Optional} con la entrada libro-ejemplares si existe
     */
    private Optional<Map.Entry<Book, Integer>> findEntryById(String bookId) {
        return bookCopies.entrySet().stream()
                .filter(e -> e.getKey().getId().equals(bookId))
                .findFirst();
    }
}
