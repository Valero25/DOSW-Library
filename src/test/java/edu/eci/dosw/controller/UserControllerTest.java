package edu.eci.dosw.controller;

import edu.eci.dosw.controller.dto.UserDTO;
import edu.eci.dosw.model.User;
import edu.eci.dosw.security.JwtService;
import edu.eci.dosw.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests para el controlador de usuarios (UserController).
 * Verifica que solo LIBRARIAN puede acceder a endpoints de usuarios.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "jwt.secret=test-secret-key-2026-must-be-at-least-256-bits-long-for-hs256",
        "jwt.expiration=3600000"
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    private String librarianToken;
    private String userToken;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Generate tokens
        librarianToken = jwtService.generateToken("librarian", "LIBRARIAN", "librarian-1");
        userToken = jwtService.generateToken("regularuser", "USER", "user-1");

        // Setup test user DTO
        userDTO = new UserDTO();
        userDTO.setId("1");
        userDTO.setName("Test User");
        userDTO.setEmail("test@example.com");
    }

    // ===== GET Requests (Only LIBRARIAN) =====

    @Test
    void getAllUsers_WithLIBRARIANToken_ShouldReturnUsers() throws Exception {
        User user = new User("1", "Test User", "test@example.com", "testuser", null, "USER", null, LocalDate.now());
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(userService).getAllUsers();
    }

    @Test
    void getAllUsers_WithUserToken_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        verify(userService, never()).getAllUsers();
    }

    @Test
    void getAllUsers_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());

        verify(userService, never()).getAllUsers();
    }

    @Test
    void getUserById_WithLIBRARIANToken_ShouldReturnUser() throws Exception {
        User user = new User("1", "Test User", "test@example.com", "testuser", null, "USER", null, LocalDate.now());
        when(userService.findUserById("1")).thenReturn(java.util.Optional.of(user));

        mockMvc.perform(get("/api/users/1")
                .header("Authorization", "Bearer " + librarianToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"));

        verify(userService).findUserById("1");
    }

    @Test
    void getUserById_WithUserToken_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/users/1")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        verify(userService, never()).findUserById("1");
    }

    // ===== POST Requests (Only LIBRARIAN) =====

    @Test
    void addUser_WithLIBRARIANToken_ShouldCreateUser() throws Exception {
        User user = new User("1", "New User", "newuser@example.com", "newuser", null, "USER", null, LocalDate.now());
        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .header("Authorization", "Bearer " + librarianToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"New User\", \"email\": \"newuser@example.com\", \"username\": \"newuser\"}")
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New User"));

        verify(userService).registerUser(any(User.class));
    }

    @Test
    void addUser_WithUserToken_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/users")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"New User\", \"email\": \"newuser@example.com\", \"username\": \"newuser\"}")
        )
                .andExpect(status().isForbidden());

        verify(userService, never()).registerUser(any(User.class));
    }

    @Test
    void addUser_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"New User\", \"email\": \"newuser@example.com\", \"username\": \"newuser\"}")
        )
                .andExpect(status().isForbidden());

        verify(userService, never()).registerUser(any(User.class));
    }
}
