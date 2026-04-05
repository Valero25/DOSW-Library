package edu.eci.dosw.persistence;

import edu.eci.dosw.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz abstracta para la persistencia de usuarios.
 */
public interface UserPersistenceRepository {

    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
