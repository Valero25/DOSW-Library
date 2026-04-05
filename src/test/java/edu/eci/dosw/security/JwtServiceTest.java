package edu.eci.dosw.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para el servicio JWT (JwtService).
 * Verifica generación, validación y extracción de claims del token.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret=test-secret-key-2026-must-be-at-least-256-bits-long-for-hs256",
        "jwt.expiration=3600000"
})
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private String token;

    @BeforeEach
    void setUp() {
        token = jwtService.generateToken("testuser", "USER", "user-1");
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void extractRole_ShouldReturnCorrectRole() {
        String role = jwtService.extractRole(token);
        assertEquals("USER", role);
    }

    @Test
    void extractUserId_ShouldReturnCorrectUserId() {
        String userId = jwtService.extractUserId(token);
        assertEquals("user-1", userId);
    }

    @Test
    void isTokenValid_WithValidToken_ShouldReturnTrue() {
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValid_WithInvalidToken_ShouldReturnFalse() {
        String invalidToken = "invalid.token.here";
        assertFalse(jwtService.isTokenValid(invalidToken));
    }

    @Test
    void generateToken_WithDifferentRoles_ShouldGenerateCorrectToken() {
        String librarianToken = jwtService.generateToken("librarian", "LIBRARIAN", "lib-1");
        
        assertEquals("librarian", jwtService.extractUsername(librarianToken));
        assertEquals("LIBRARIAN", jwtService.extractRole(librarianToken));
        assertEquals("lib-1", jwtService.extractUserId(librarianToken));
        assertTrue(jwtService.isTokenValid(librarianToken));
    }

    @Test
    void generateToken_MultipleTokens_AllValid() {
        String token1 = jwtService.generateToken("user1", "USER", "1");
        String token2 = jwtService.generateToken("user2", "LIBRARIAN", "2");
        String token3 = jwtService.generateToken("user3", "USER", "3");
        
        assertTrue(jwtService.isTokenValid(token1));
        assertTrue(jwtService.isTokenValid(token2));
        assertTrue(jwtService.isTokenValid(token3));
        
        assertEquals("user1", jwtService.extractUsername(token1));
        assertEquals("user2", jwtService.extractUsername(token2));
        assertEquals("user3", jwtService.extractUsername(token3));
    }
}
