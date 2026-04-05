package edu.eci.dosw.persistence.nonrelational.mapper;

import edu.eci.dosw.model.Loan;
import edu.eci.dosw.model.LoanHistory;
import edu.eci.dosw.persistence.nonrelational.document.LoanDocument;

import java.util.stream.Collectors;

public class LoanDocumentMapper {

    private LoanDocumentMapper() {}

    public static Loan toDomain(LoanDocument doc) {
        Loan loan = new Loan();
        loan.setId(doc.getId());
        loan.setLoanDate(doc.getLoanDate());
        loan.setReturnDate(doc.getReturnDate());
        loan.setReturned(doc.isReturned());
        if (doc.getLoanHistory() != null) {
            loan.setLoanHistory(doc.getLoanHistory().stream()
                    .map(h -> new LoanHistory(h.getStatus(), h.getDate()))
                    .collect(Collectors.toList()));
        }
        return loan;
    }

    public static LoanDocument toDocument(Loan loan, String bookId, String userId) {
        LoanDocument doc = new LoanDocument();
        doc.setId(loan.getId());
        doc.setBookId(bookId);
        doc.setUserId(userId);
        doc.setLoanDate(loan.getLoanDate());
        doc.setReturnDate(loan.getReturnDate());
        doc.setReturned(loan.isReturned());
        if (loan.getLoanHistory() != null) {
            doc.setLoanHistory(loan.getLoanHistory().stream()
                    .map(h -> new LoanDocument.LoanHistoryEmbedded(h.getStatus(), h.getDate()))
                    .collect(Collectors.toList()));
        }
        return doc;
    }
}
