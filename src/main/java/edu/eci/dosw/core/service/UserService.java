package edu.eci.dosw.core.service;

import edu.eci.dosw.core.model.User;
import edu.eci.dosw.core.util.IdGeneratorUtil;
import edu.eci.dosw.core.util.ValidationUtil;
import edu.eci.dosw.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de gestionar el registro y administración de usuarios de la biblioteca.
 * Provee operaciones para registrar, consultar, actualizar y eliminar usuarios.
 */
@Service("coreUserService")
public class UserService {

    private final List<User> users = new ArrayList<>();

    /**
     * Registra un nuevo usuario en el sistema.
     * Si el usuario no posee identificador, se genera uno automáticamente.
     *
     * @param user usuario a registrar
     * @return el usuario registrado con su identificador asignado
     * @throws IllegalArgumentException si el usuario es nulo o el nombre está vacío
     */
    public User registerUser(User user) {
        ValidationUtil.validateNotNull(user, "usuario");
        ValidationUtil.validateNotBlank(user.getName(), "nombre");
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(IdGeneratorUtil.generate());
        }
        users.add(user);
        return user;
    }

    /**
     * Retorna todos los usuarios registrados en el sistema.
     *
     * @return lista con todos los usuarios
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Busca un usuario por su identificador único.
     *
     * @param id identificador del usuario
     * @return un {@link Optional} con el usuario si existe, o vacío si no se encuentra
     */
    public Optional<User> findUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    /**
     * Recupera un usuario por su identificador; lanza excepción si no existe.
     *
     * @param id identificador del usuario
     * @return el usuario encontrado
     * @throws UserNotFoundException si no existe un usuario con el identificador indicado
     */
    public User getUserOrThrow(String id) {
        return findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Actualiza los datos del usuario cuyo identificador coincide con el del objeto recibido.
     *
     * @param updated objeto usuario con los nuevos datos; debe tener un identificador válido
     * @return el usuario actualizado
     * @throws UserNotFoundException    si no existe el usuario con ese identificador
     * @throws IllegalArgumentException si el usuario es nulo o el nombre está vacío
     */
    public User updateUser(User updated) {
        ValidationUtil.validateNotNull(updated, "usuario");
        ValidationUtil.validateNotBlank(updated.getName(), "nombre");
        User existing = getUserOrThrow(updated.getId());
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        return existing;
    }

    /**
     * Elimina un usuario del sistema por su identificador.
     *
     * @param id identificador del usuario a eliminar
     * @return {@code true} si fue eliminado; {@code false} si no se encontró
     */
    public boolean removeUser(String id) {
        return users.removeIf(u -> u.getId().equals(id));
    }
}
