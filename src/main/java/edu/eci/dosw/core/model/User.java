package edu.eci.dosw.core.model;

import edu.eci.dosw.core.model.enums.MembershipType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Representa un usuario registrado en el sistema de la biblioteca.
 * Almacena los datos de identificación y contacto del usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String name;
    private String email;
    
    private MembershipType membershipType;
    private LocalDate dateAddedAsUser;
}
