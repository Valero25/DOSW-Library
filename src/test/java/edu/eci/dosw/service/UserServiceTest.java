package edu.eci.dosw.service;

import edu.eci.dosw.model.User;
import edu.eci.dosw.persistence.UserPersistenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserPersistenceRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user-1");
        user.setName("Juan");
        user.setEmail("juan@eci.edu.co");
        user.setUsername("juan");
        user.setPassword("encoded");
        user.setRole("USER");
    }

    @Test
    void registerUser_ShouldRegisterSuccessfully() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User input = new User();
        input.setName("Juan");
        input.setEmail("juan@eci.edu.co");
        input.setUsername("juan");
        input.setPassword("pass123");

        User result = userService.registerUser(input);

        assertNotNull(result.getId());
        assertEquals("Juan", result.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenNameIsBlank() {
        User input = new User();
        input.setName("  ");
        input.setEmail("test@eci.edu.co");

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(input));
    }

    @Test
    void registerUser_ShouldThrowException_WhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null));
    }

    @Test
    void getAllUsers_ShouldReturnAllRegisteredUsers() {
        User user2 = new User();
        user2.setId("user-2");
        user2.setName("Ana");
        user2.setEmail("ana@eci.edu.co");

        when(userRepository.findAll()).thenReturn(List.of(user, user2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void findUserById_ShouldReturnUser_WhenExists() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));

        Optional<User> found = userService.findUserById("user-1");

        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getName());
    }

    @Test
    void findUserById_ShouldReturnEmpty_WhenNotExists() {
        when(userRepository.findById("non-existent")).thenReturn(Optional.empty());

        Optional<User> found = userService.findUserById("non-existent");
        assertTrue(found.isEmpty());
    }
}
