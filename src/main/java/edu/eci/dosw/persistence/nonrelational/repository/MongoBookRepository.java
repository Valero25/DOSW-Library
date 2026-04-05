package edu.eci.dosw.persistence.nonrelational.repository;

import edu.eci.dosw.persistence.nonrelational.document.BookDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoBookRepository extends MongoRepository<BookDocument, String> {

    Optional<BookDocument> findByIsbn(String isbn);
}
