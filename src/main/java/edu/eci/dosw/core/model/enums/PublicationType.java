package edu.eci.dosw.core.model.enums;

/**
 * Enumeración que representa los tipos de publicación disponibles en la biblioteca.
 */
public enum PublicationType {
    LIBRO("Libro"),
    REVISTA("Revista"),
    EBOOK("eBook"),
    CARTILLA("Cartilla"),
    PERIODICO("Periódico"),
    TESIS("Tesis"),
    ARTICULO("Artículo");

    private final String descripcion;

    PublicationType(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
