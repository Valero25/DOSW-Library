package edu.eci.dosw.service;

import edu.eci.dosw.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void registerUser_ShouldRegisterSuccessfully() {
        User user = new User(null, "Juan", "juan@eci.edu.co");
        User result = userService.registerUser(user);

        assertNotNull(result.getId());
        assertEquals("Juan", result.getName());
    }

    @Test
    void registerUser_ShouldThrowException_WhenNameIsBlank() {
        User user = new User(null, " ", "juan@eci.edu.co");
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void registerUser_ShouldThrowException_WhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null));
    }

    @Test
    void getAllUsers_ShouldReturnAllRegisteredUsers() {
        userService.registerUser(new User(null, "User 1", "u1@eci.edu.co"));
        userService.registerUser(new User(null, "User 2", "u2@eci.edu.co"));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void findUserById_ShouldReturnUser_WhenExists() {
        User user = userService.registerUser(new User(null, "Ana", "ana@eci.edu.co"));
        Optional<User> found = userService.findUserById(user.getId());

        assertTrue(found.isPresent());
        assertEquals("Ana", found.get().getName());
    }

    @Test
    void findUserById_ShouldReturnEmpty_WhenNotExists() {
        Optional<User> found = userService.findUserById("non-existent-id");
        assertTrue(found.isEmpty());
    }
}
