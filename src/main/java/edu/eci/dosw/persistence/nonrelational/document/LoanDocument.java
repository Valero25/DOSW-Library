package edu.eci.dosw.persistence.nonrelational.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanDocument {

    @Id
    private String id;

    private String bookId;
    private String userId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private boolean returned;

    // Historial embebido
    private List<LoanHistoryEmbedded> loanHistory = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoanHistoryEmbedded {
        private String status;
        private LocalDate date;
    }
}
