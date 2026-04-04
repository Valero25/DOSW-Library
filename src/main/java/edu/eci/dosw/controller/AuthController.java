package edu.eci.dosw.controller;

import edu.eci.dosw.persistence.entity.UserEntity;
import edu.eci.dosw.persistence.repository.UserRepository;
import edu.eci.dosw.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * Controlador de autenticación y registro de usuarios.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints de login y registro")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Login", description = "Autentica un usuario y retorna un token JWT")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        UserEntity user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole(), user.getId());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "username", user.getUsername(),
                "role", user.getRole(),
                "userId", user.getId()
        ));
    }

    @Operation(summary = "Registro", description = "Registra un nuevo usuario en el sistema")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String name = body.get("name");
        String email = body.get("email");
        String role = body.getOrDefault("role", "USER");

        if (username == null || password == null || name == null || email == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Los campos username, password, name y email son obligatorios"));
        }

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El username ya está en uso"));
        }

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El email ya está en uso"));
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setEmail(email);
        user.setRole(role.toUpperCase());
        user.setDateAddedAsUser(LocalDate.now());

        UserEntity saved = userRepository.save(user);

        String token = jwtService.generateToken(saved.getUsername(), saved.getRole(), saved.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "token", token,
                "username", saved.getUsername(),
                "role", saved.getRole(),
                "userId", saved.getId()
        ));
    }
}
