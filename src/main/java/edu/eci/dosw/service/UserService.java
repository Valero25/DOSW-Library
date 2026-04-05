package edu.eci.dosw.service;

import edu.eci.dosw.model.User;
import edu.eci.dosw.persistence.UserPersistenceRepository;
import edu.eci.dosw.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar los usuarios de la biblioteca.
 */
@Service
public class UserService {

    private final UserPersistenceRepository userRepository;

    public UserService(UserPersistenceRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     */
    public User registerUser(User user) {
        ValidationUtil.validateNotNull(user, "user");
        ValidationUtil.validateNotBlank(user.getName(), "name");

        if (user.getDateAddedAsUser() == null) {
            user.setDateAddedAsUser(LocalDate.now());
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }

        return userRepository.save(user);
    }

    /**
     * Retorna todos los usuarios registrados.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Busca un usuario por su ID.
     */
    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Busca un usuario por su username.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
