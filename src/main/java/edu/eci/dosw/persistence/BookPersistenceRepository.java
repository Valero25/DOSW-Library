package edu.eci.dosw.persistence;

import edu.eci.dosw.model.Book;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz abstracta para la persistencia de libros.
 * Puede ser implementada por JPA (relational) o MongoDB (nonrelational).
 */
public interface BookPersistenceRepository {

    Book save(Book book);

    Optional<Book> findById(String id);

    List<Book> findAll();

    void deleteById(String id);

    boolean existsById(String id);
}
