package edu.eci.dosw.controller;

import edu.eci.dosw.persistence.entity.UserEntity;
import edu.eci.dosw.persistence.repository.UserRepository;
import edu.eci.dosw.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests para el controlador de autenticación (AuthController).
 * Verifica el flujo de login y registro con tokens JWT.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "jwt.secret=test-secret-key-2026-must-be-at-least-256-bits-long-for-hs256",
        "jwt.expiration=3600000"
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId("user-1");
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setRole("USER");
        testUser.setDateAddedAsUser(LocalDate.now());
    }

    @Test
    void login_WithValidCredentials_ShouldReturnJwtToken() throws Exception {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testuser\", \"password\": \"password123\"}")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.userId").value("user-1"));

        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void login_WithInvalidPassword_ShouldReturnUnauthorized() throws Exception {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testuser\", \"password\": \"wrongpassword\"}")
        )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenciales inválidas"));
    }

    @Test
    void login_WithNonExistentUser_ShouldReturnUnauthorized() throws Exception {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"nonexistent\", \"password\": \"password123\"}")
        )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenciales inválidas"));
    }

    @Test
    void register_WithValidData_ShouldCreateUserAndReturnToken() throws Exception {
        UserEntity savedUser = new UserEntity();
        savedUser.setId("new-user-1");
        savedUser.setUsername("newuser");
        savedUser.setPassword(passwordEncoder.encode("password123"));
        savedUser.setName("New User");
        savedUser.setEmail("newuser@example.com");
        savedUser.setRole("USER");
        savedUser.setDateAddedAsUser(LocalDate.now());

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"newuser\", \"password\": \"password123\", " +
                        "\"name\": \"New User\", \"email\": \"newuser@example.com\"}")
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmail("newuser@example.com");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void register_WithExistingUsername_ShouldReturnBadRequest() throws Exception {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testuser\", \"password\": \"password123\", " +
                        "\"name\": \"Test User\", \"email\": \"test@example.com\"}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El username ya está en uso"));
    }

    @Test
    void register_WithExistingEmail_ShouldReturnBadRequest() throws Exception {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"newuser\", \"password\": \"password123\", " +
                        "\"name\": \"New User\", \"email\": \"test@example.com\"}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El email ya está en uso"));
    }

    @Test
    void register_WithMissingFields_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"newuser\", \"password\": \"password123\"}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    void register_WithLIBRARIANRole_ShouldCreateLIBRARIANUser() throws Exception {
        UserEntity librarianUser = new UserEntity();
        librarianUser.setId("librarian-1");
        librarianUser.setUsername("librarianuser");
        librarianUser.setPassword(passwordEncoder.encode("password123"));
        librarianUser.setName("Librarian User");
        librarianUser.setEmail("librarian@example.com");
        librarianUser.setRole("LIBRARIAN");
        librarianUser.setDateAddedAsUser(LocalDate.now());

        when(userRepository.existsByUsername("librarianuser")).thenReturn(false);
        when(userRepository.existsByEmail("librarian@example.com")).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(librarianUser);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"librarianuser\", \"password\": \"password123\", " +
                        "\"name\": \"Librarian User\", \"email\": \"librarian@example.com\", \"role\": \"LIBRARIAN\"}")
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("LIBRARIAN"));
    }
}
