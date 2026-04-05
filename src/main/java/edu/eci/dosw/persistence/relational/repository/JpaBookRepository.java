package edu.eci.dosw.persistence.relational.repository;

import edu.eci.dosw.persistence.relational.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaBookRepository extends JpaRepository<BookEntity, String> {

    Optional<BookEntity> findByIsbn(String isbn);
}
