package edu.eci.dosw.service;

import edu.eci.dosw.model.User;
import edu.eci.dosw.persistence.entity.UserEntity;
import edu.eci.dosw.persistence.repository.UserRepository;
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
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId("user-1");
        userEntity.setName("Juan");
        userEntity.setEmail("juan@eci.edu.co");
        userEntity.setUsername("juan");
        userEntity.setPassword("encoded");
        userEntity.setRole("USER");
    }

    @Test
    void registerUser_ShouldRegisterSuccessfully() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User user = new User();
        user.setName("Juan");
        user.setEmail("juan@eci.edu.co");
        user.setUsername("juan");
        user.setPassword("pass123");

        User result = userService.registerUser(user);

        assertNotNull(result.getId());
        assertEquals("Juan", result.getName());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenNameIsBlank() {
        User user = new User();
        user.setName("  ");
        user.setEmail("test@eci.edu.co");

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void registerUser_ShouldThrowException_WhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null));
    }

    @Test
    void getAllUsers_ShouldReturnAllRegisteredUsers() {
        UserEntity entity2 = new UserEntity();
        entity2.setId("user-2");
        entity2.setName("Ana");
        entity2.setEmail("ana@eci.edu.co");

        when(userRepository.findAll()).thenReturn(List.of(userEntity, entity2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void findUserById_ShouldReturnUser_WhenExists() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(userEntity));

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
