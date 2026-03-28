package edu.eci.dosw.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Objeto de transferencia de datos (DTO) que representa la información de un libro
 * expuesta a través de la capa de controladores REST.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representación de un libro en el catálogo")
public class BookDTO {

    @Schema(description = "Identificador único del libro", example = "b1a2c3d4-e5f6-7890-abcd-ef1234567890")
    private String id;

    @Schema(description = "Título del libro", example = "Clean Code")
    private String title;

    @Schema(description = "Autor del libro", example = "Robert C. Martin")
    private String author;

    @Schema(description = "Código ISBN del libro", example = "9780132350884")
    private String isbn;

    @Schema(description = "Indica si el libro está disponible para préstamo", example = "true")
    private boolean available;
    
    @Schema(description = "Categorías a las que pertenece el libro", example = "[\"Programación\", \"Desarrollo de Software\"]")
    private List<String> categories;
    
    @Schema(description = "Tipo de publicación", example = "LIBRO")
    private String publicationType;
    
    @Schema(description = "Fecha de publicación del libro", example = "2008-08-01")
    private LocalDate publicationDate;
    
    @Schema(description = "Información de metadata del libro")
    private MetadataDTO metadata;
    
    @Schema(description = "Información de disponibilidad del libro")
    private AvailabilityDTO availability;
    
    @Schema(description = "Fecha en que el libro fue agregado al catálogo", example = "2026-03-27")
    private LocalDate dateAddedToCatalog;
}

