package edu.eci.dosw.model;

import edu.eci.dosw.model.enums.MembershipType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a library user.
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
