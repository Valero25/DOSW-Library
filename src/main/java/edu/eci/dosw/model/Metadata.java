package edu.eci.dosw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la información de metadata de un libro.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    private Integer pages;
    private String language;
    private String publisher;
}
