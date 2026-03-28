package edu.eci.dosw.core.model.enums;

/**
 * Enumeración que representa los tipos de membresía disponibles para los usuarios.
 */
public enum MembershipType {
    STANDARD("Standard"),
    PLATINUM("Platinum"),
    VIP("VIP");

    private final String descripcion;

    MembershipType(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
