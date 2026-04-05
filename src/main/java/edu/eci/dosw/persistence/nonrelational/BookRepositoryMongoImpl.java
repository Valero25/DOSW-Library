package edu.eci.dosw.persistence.nonrelational;

import edu.eci.dosw.model.Book;
import edu.eci.dosw.persistence.BookPersistenceRepository;
import edu.eci.dosw.persistence.nonrelational.mapper.BookDocumentMapper;
import edu.eci.dosw.persistence.nonrelational.repository.MongoBookRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
public class BookRepositoryMongoImpl implements BookPersistenceRepository {

    private final MongoBookRepository repository;

    public BookRepositoryMongoImpl(MongoBookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return BookDocumentMapper.toDomain(
                repository.save(BookDocumentMapper.toDocument(book)));
    }

    @Override
    public Optional<Book> findById(String id) {
        return repository.findById(id).map(BookDocumentMapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll().stream()
                .map(BookDocumentMapper::toDomain)
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
