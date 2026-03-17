package edu.eci.dosw.core.model;

/**
 * Representa un usuario registrado en el sistema de la biblioteca.
 * Almacena los datos de identificación y contacto del usuario.
 */
public class User {

    private String id;
    private String name;
    private String email;

    /**
     * Constructor por defecto.
     */
    public User() {
    }

    /**
     * Constructor con todos los parámetros requeridos.
     *
     * @param id    identificador único del usuario
     * @param name  nombre completo del usuario
     * @param email dirección de correo electrónico del usuario
     */
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Retorna el identificador único del usuario.
     *
     * @return identificador del usuario
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     *
     * @param id identificador del usuario
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna el nombre completo del usuario.
     *
     * @return nombre del usuario
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre completo del usuario.
     *
     * @param name nombre del usuario
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna la dirección de correo electrónico del usuario.
     *
     * @return correo electrónico del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece la dirección de correo electrónico del usuario.
     *
     * @param email correo electrónico del usuario
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna una representación en cadena del usuario.
     *
     * @return representación textual del objeto
     */
    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', email='" + email + "'}";
    }
}
