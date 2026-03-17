package edu.eci.dosw.service;

import edu.eci.dosw.model.User;
import edu.eci.dosw.util.ValidationUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class encapsulating user-related operations.
 */
@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    /**
     * Registers a new user in the system.
     *
     * @param user the user to register
     * @return the registered user
     */
    public User registerUser(User user) {
        ValidationUtil.validateNotNull(user, "user");
        ValidationUtil.validateNotBlank(user.getName(), "name");
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(UUID.randomUUID().toString());
        }
        users.add(user);
        return user;
    }

    /**
     * Returns all registered users.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the user ID
     * @return Optional containing the user if found
     */
    public Optional<User> findUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }
}
