package edu.eci.dosw.model;

import edu.eci.dosw.model.enums.PublicationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a book in the library system.
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
