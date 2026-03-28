package edu.eci.dosw.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Objeto de transferencia de datos (DTO) que representa la información de un usuario
 * expuesta a través de la capa de controladores REST.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representación de un usuario registrado")
public class UserDTO {

    @Schema(description = "Identificador único del usuario", example = "u1a2c3d4-e5f6-7890-abcd-ef1234567890")
    private String id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String name;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@mail.com")
    private String email;
    
    @Schema(description = "Tipo de membresía del usuario", example = "STANDARD")
    private String membershipType;
    
    @Schema(description = "Fecha en que el usuario fue registrado en la biblioteca", example = "2026-03-27")
    private LocalDate dateAddedAsUser;
}

