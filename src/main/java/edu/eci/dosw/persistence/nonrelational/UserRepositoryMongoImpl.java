package edu.eci.dosw.persistence.nonrelational;

import edu.eci.dosw.model.User;
import edu.eci.dosw.persistence.UserPersistenceRepository;
import edu.eci.dosw.persistence.nonrelational.mapper.UserDocumentMapper;
import edu.eci.dosw.persistence.nonrelational.repository.MongoUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
public class UserRepositoryMongoImpl implements UserPersistenceRepository {

    private final MongoUserRepository repository;

    public UserRepositoryMongoImpl(MongoUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return UserDocumentMapper.toDomain(
                repository.save(UserDocumentMapper.toDocument(user)));
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id).map(UserDocumentMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(UserDocumentMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream()
                .map(UserDocumentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
