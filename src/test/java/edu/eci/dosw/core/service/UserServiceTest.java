package edu.eci.dosw.core.service;

import edu.eci.dosw.core.model.User;
import edu.eci.dosw.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link UserService} del paquete core,
 * verificando los escenarios exitosos y de error estipulados en el diseño.
 */
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    private User createUser(String id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    // --- ESCENARIOS EXITOSOS ---

    @Test
    void registrarUsuario_DebeRegistrarseExitosamente() {
        User usuario = createUser(null, "Juan", "juan@eci.edu.co");
        User resultado = userService.registerUser(usuario);

        assertNotNull(resultado.getId());
        assertEquals("Juan", resultado.getName());
    }

    @Test
    void obtenerTodosLosUsuarios_DebeRetornarTodosLosRegistrados() {
        userService.registerUser(createUser(null, "U1", "u1@eci.edu.co"));
        userService.registerUser(createUser(null, "U2", "u2@eci.edu.co"));
        List<User> usuarios = userService.getAllUsers();
        assertEquals(2, usuarios.size());
    }

    @Test
    void buscarUsuarioPorId_DebeRetornarUsuario_CuandoExiste() {
        User usuario = userService.registerUser(createUser(null, "Ana", "ana@eci.edu.co"));
        Optional<User> encontrado = userService.findUserById(usuario.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Ana", encontrado.get().getName());
    }

    @Test
    void actualizarUsuario_DebeActualizarDatosExitosamente() {
        User usuario = userService.registerUser(createUser(null, "Original", "o@eci.edu.co"));
        User actualizado = createUser(usuario.getId(), "Nuevo", "n@eci.edu.co");
        User resultado = userService.updateUser(actualizado);

        assertEquals("Nuevo", resultado.getName());
        assertEquals("n@eci.edu.co", resultado.getEmail());
    }

    @Test
    void eliminarUsuario_DebeRetornarVerdadero_YBorrarlo_CuandoExiste() {
        User usuario = userService.registerUser(createUser(null, "Eliminar", "eliminar@eci.edu.co"));
        assertTrue(userService.removeUser(usuario.getId()));
        assertTrue(userService.getAllUsers().isEmpty());
    }

    // --- ESCENARIOS DE ERROR ---

    @Test
    void registrarUsuario_DebeLanzarExcepcion_CuandoUsuarioNuloONombreVacio() {
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null));
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(createUser(null, "  ", "e")));
    }

    @Test
    void buscarUsuarioPorId_DebeRetornarVacio_CuandoNoExiste() {
        Optional<User> encontrado = userService.findUserById("id-falso");
        assertTrue(encontrado.isEmpty());
    }

    @Test
    void obtenerUsuarioOLanzarExcepcion_DebeLanzarExcepcion_CuandoNoExiste() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserOrThrow("falso"));
    }

    @Test
    void actualizarUsuario_DebeLanzarExcepcion_CuandoUsuarioNoExiste() {
        User actualizado = createUser("falso", "Nuevo", "correo");
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(actualizado));
    }
}
