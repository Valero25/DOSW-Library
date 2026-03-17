package edu.eci.dosw.controller.dto;

/**
 * Objeto de transferencia de datos (DTO) que representa la información de un usuario
 * expuesta a través de la capa de controladores REST.
 */
public class UserDTO {

    private String id;
    private String name;
    private String email;

    /**
     * Constructor por defecto.
     */
    public UserDTO() {
    }

    /**
     * Constructor con todos los campos del DTO.
     *
     * @param id    identificador único del usuario
     * @param name  nombre completo del usuario
     * @param email correo electrónico del usuario
     */
    public UserDTO(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Retorna el identificador del usuario.
     *
     * @return identificador del usuario
     */
    public String getId() { return id; }

    /**
     * Establece el identificador del usuario.
     *
     * @param id identificador del usuario
     */
    public void setId(String id) { this.id = id; }

    /**
     * Retorna el nombre completo del usuario.
     *
     * @return nombre del usuario
     */
    public String getName() { return name; }

    /**
     * Establece el nombre completo del usuario.
     *
     * @param name nombre del usuario
     */
    public void setName(String name) { this.name = name; }

    /**
     * Retorna el correo electrónico del usuario.
     *
     * @return correo electrónico del usuario
     */
    public String getEmail() { return email; }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email correo electrónico del usuario
     */
    public void setEmail(String email) { this.email = email; }
}
