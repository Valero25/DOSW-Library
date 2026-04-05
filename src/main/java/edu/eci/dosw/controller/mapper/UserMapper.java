package edu.eci.dosw.controller.mapper;

import edu.eci.dosw.controller.dto.UserDTO;
import edu.eci.dosw.core.model.User;

/**
 * Clase responsable de convertir entre la entidad {@link User} y su correspondiente
 * objeto de transferencia de datos {@link UserDTO}.
 * Desacopla la representación interna del dominio de la representación expuesta por la API REST.
 */
public class UserMapper {

    /**
     * Constructor privado para evitar la instanciación de esta clase utilitaria.
     */
    private UserMapper() {
    }

    /**
     * Convierte una entidad {@link User} en su representación {@link UserDTO}.
     *
     * @param user entidad usuario a convertir; no debe ser nula
     * @return DTO con los datos del usuario
     */
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        
        if (user.getMembershipType() != null) {
            dto.setMembershipType(user.getMembershipType().toString());
        }
        
        dto.setDateAddedAsUser(user.getDateAddedAsUser());
        
        return dto;
    }

    /**
     * Convierte un {@link UserDTO} en la entidad de dominio {@link User}.
     *
     * @param dto DTO del usuario a convertir; no debe ser nulo
     * @return entidad usuario correspondiente al DTO
     */
    public static User toModel(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        
        if (dto.getMembershipType() != null) {
            try {
                user.setMembershipType(Enum.valueOf(edu.eci.dosw.core.model.enums.MembershipType.class, dto.getMembershipType()));
            } catch (IllegalArgumentException e) {
                user.setMembershipType(null);
            }
        }
        
        user.setDateAddedAsUser(dto.getDateAddedAsUser());
        
        return user;
    }
}
