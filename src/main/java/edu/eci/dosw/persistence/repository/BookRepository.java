package edu.eci.dosw.persistence.repository;

import edu.eci.dosw.persistence.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {

    Optional<BookEntity> findByIsbn(String isbn);
}
