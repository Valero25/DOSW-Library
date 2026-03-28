package edu.eci.dosw.core.model;

import edu.eci.dosw.core.model.enums.PublicationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * Representa un libro en el catálogo de la biblioteca.
 * Contiene la información básica del libro y su estado de disponibilidad.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Book {

    private String id;
    private String title;
    private String author;
    private String isbn;
    private boolean available;
    
    private List<String> categories;
    private PublicationType publicationType;
    private LocalDate publicationDate;
    private Metadata metadata;
    private Availability availability;
    private LocalDate dateAddedToCatalog;

    public Book(String id, String title, String author, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = true;
    }
}
