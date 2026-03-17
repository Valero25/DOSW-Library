package edu.eci.dosw.controller.mapper;

import edu.eci.dosw.controller.dto.UserDTO;
import edu.eci.dosw.core.model.User;

/**
 * Clase responsable de convertir entre la entidad {@link User} y su correspondiente
 * objeto de transferencia de datos {@link UserDTO}.
 * Desacopla la representación interna del dominio de la representación expuesta por la API REST.
 */
public class UserMapper {

    private UserMapper() {
        // Clase utilitaria — no se permite instanciar
    }

    /**
     * Convierte una entidad {@link User} en su representación {@link UserDTO}.
     *
     * @param user entidad usuario a convertir; no debe ser nula
     * @return DTO con los datos del usuario
     */
    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    /**
     * Convierte un {@link UserDTO} en la entidad de dominio {@link User}.
     *
     * @param dto DTO del usuario a convertir; no debe ser nulo
     * @return entidad usuario correspondiente al DTO
     */
    public static User toModel(UserDTO dto) {
        return new User(
                dto.getId(),
                dto.getName(),
                dto.getEmail()
        );
    }
}
