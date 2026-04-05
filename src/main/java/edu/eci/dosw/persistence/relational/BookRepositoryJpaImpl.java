package edu.eci.dosw.persistence.relational;

import edu.eci.dosw.model.Book;
import edu.eci.dosw.persistence.BookPersistenceRepository;
import edu.eci.dosw.persistence.relational.mapper.BookEntityMapper;
import edu.eci.dosw.persistence.relational.repository.JpaBookRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("relational")
public class BookRepositoryJpaImpl implements BookPersistenceRepository {

    private final JpaBookRepository repository;

    public BookRepositoryJpaImpl(JpaBookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return BookEntityMapper.toDomain(
                repository.save(BookEntityMapper.toEntity(book)));
    }

    @Override
    public Optional<Book> findById(String id) {
        return repository.findById(id).map(BookEntityMapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll().stream()
                .map(BookEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }
}
