package edu.eci.dosw.controller;

import edu.eci.dosw.model.User;
import edu.eci.dosw.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user-related operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final LibraryService libraryService;

    public UserController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * Retrieves all registered users.
     *
     * @return list of all users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(libraryService.getAllUsers());
    }

    /**
     * Retrieves a single user by their ID.
     *
     * @param id the user ID
     * @return the user if found, 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return libraryService.findUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Registers a new user.
     *
     * @param user the user to register
     * @return the created user
     */
    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User created = libraryService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
