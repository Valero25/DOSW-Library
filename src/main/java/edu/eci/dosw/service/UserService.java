package edu.eci.dosw.service;

import edu.eci.dosw.model.User;
import edu.eci.dosw.persistence.entity.UserEntity;
import edu.eci.dosw.persistence.mapper.UserEntityMapper;
import edu.eci.dosw.persistence.repository.UserRepository;
import edu.eci.dosw.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar los usuarios de la biblioteca.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
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

        UserEntity entity = UserEntityMapper.toEntity(user);
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());

        UserEntity saved = userRepository.save(entity);
        return UserEntityMapper.toDomain(saved);
    }

    /**
     * Retorna todos los usuarios registrados.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Busca un usuario por su ID.
     */
    public Optional<User> findUserById(String id) {
        return userRepository.findById(id)
                .map(UserEntityMapper::toDomain);
    }

    /**
     * Busca un usuario por su username.
     */
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
