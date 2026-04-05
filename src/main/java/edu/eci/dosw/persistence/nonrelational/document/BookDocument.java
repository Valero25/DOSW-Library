package edu.eci.dosw.persistence.nonrelational.document;

import edu.eci.dosw.model.enums.PublicationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDocument {

    @Id
    private String id;

    private String title;
    private String author;
    private String isbn;
    private boolean available;

    private List<String> categories;
    private PublicationType publicationType;
    private LocalDate publicationDate;

    // Metadata embebido
    private MetadataEmbedded metadata;

    // Availability embebido
    private AvailabilityEmbedded availability;

    private LocalDate dateAddedToCatalog;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetadataEmbedded {
        private Integer pages;
        private String language;
        private String publisher;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AvailabilityEmbedded {
        private String status;
        private Integer totalCopies;
        private Integer availableCopies;
        private Integer loanedCopies;
    }
}
