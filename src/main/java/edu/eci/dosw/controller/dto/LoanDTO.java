package edu.eci.dosw.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Objeto de transferencia de datos (DTO) que representa la información de un préstamo
 * expuesta a través de la capa de controladores REST.
 * Utiliza identificadores de libro y usuario en lugar de objetos anidados completos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representación de un préstamo de libro")
public class LoanDTO {

    @Schema(description = "Identificador único del préstamo", example = "l1a2c3d4-e5f6-7890-abcd-ef1234567890")
    private String id;

    @Schema(description = "Identificador del libro prestado", example = "b1a2c3d4-e5f6-7890-abcd-ef1234567890")
    private String bookId;

    @Schema(description = "Identificador del usuario que realizó el préstamo", example = "u1a2c3d4-e5f6-7890-abcd-ef1234567890")
    private String userId;

    @Schema(description = "Fecha de realización del préstamo", example = "2026-03-27")
    private LocalDate loanDate;

    @Schema(description = "Fecha de devolución del libro", example = "2026-04-10")
    private LocalDate returnDate;

    @Schema(description = "Indica si el préstamo fue completado", example = "false")
    private boolean returned;
    
    @Schema(description = "Historial de eventos del préstamo")
    private List<LoanHistoryDTO> loanHistory;
}

