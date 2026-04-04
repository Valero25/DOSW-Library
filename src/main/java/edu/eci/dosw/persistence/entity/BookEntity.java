package edu.eci.dosw.persistence.entity;

import edu.eci.dosw.model.enums.PublicationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true)
    private String isbn;

    private boolean available;

    @ElementCollection
    @CollectionTable(name = "book_categories", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "category")
    private List<String> categories;

    @Enumerated(EnumType.STRING)
    private PublicationType publicationType;

    private LocalDate publicationDate;

    // Metadata embebido
    private Integer pages;
    private String language;
    private String publisher;

    // Availability embebido
    private String status;
    private Integer totalCopies;
    private Integer availableCopies;
    private Integer loanedCopies;

    private LocalDate dateAddedToCatalog;
}
