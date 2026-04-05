package edu.eci.dosw.persistence.relational;

import edu.eci.dosw.model.User;
import edu.eci.dosw.persistence.UserPersistenceRepository;
import edu.eci.dosw.persistence.relational.entity.UserEntity;
import edu.eci.dosw.persistence.relational.mapper.UserEntityMapper;
import edu.eci.dosw.persistence.relational.repository.JpaUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("relational")
public class UserRepositoryJpaImpl implements UserPersistenceRepository {

    private final JpaUserRepository repository;

    public UserRepositoryJpaImpl(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserEntityMapper.toEntity(user);
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        return UserEntityMapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id).map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(e -> {
            User u = UserEntityMapper.toDomain(e);
            u.setUsername(e.getUsername());
            u.setPassword(e.getPassword());
            u.setRole(e.getRole());
            return u;
        });
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream()
                .map(UserEntityMapper::toDomain)
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
