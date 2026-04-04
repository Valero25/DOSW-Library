package edu.eci.dosw.persistence.mapper;

import edu.eci.dosw.model.Loan;
import edu.eci.dosw.model.LoanHistory;
import edu.eci.dosw.persistence.entity.LoanEntity;
import edu.eci.dosw.persistence.entity.LoanHistoryEntity;

import java.util.stream.Collectors;

public class LoanEntityMapper {

    private LoanEntityMapper() {
    }

    public static Loan toDomain(LoanEntity entity) {
        Loan loan = new Loan();
        loan.setId(entity.getId());
        loan.setBook(BookEntityMapper.toDomain(entity.getBook()));
        loan.setUser(UserEntityMapper.toDomain(entity.getUser()));
        loan.setLoanDate(entity.getLoanDate());
        loan.setReturnDate(entity.getReturnDate());
        loan.setReturned(entity.isReturned());

        if (entity.getLoanHistory() != null) {
            loan.setLoanHistory(entity.getLoanHistory().stream()
                    .map(h -> new LoanHistory(h.getStatus(), h.getDate()))
                    .collect(Collectors.toList()));
        }

        return loan;
    }

    public static LoanEntity toEntity(Loan loan) {
        LoanEntity entity = new LoanEntity();
        entity.setId(loan.getId());
        entity.setLoanDate(loan.getLoanDate());
        entity.setReturnDate(loan.getReturnDate());
        entity.setReturned(loan.isReturned());

        if (loan.getLoanHistory() != null) {
            entity.setLoanHistory(loan.getLoanHistory().stream()
                    .map(h -> {
                        LoanHistoryEntity he = new LoanHistoryEntity();
                        he.setStatus(h.getStatus());
                        he.setDate(h.getDate());
                        he.setLoan(entity);
                        return he;
                    })
                    .collect(Collectors.toList()));
        }

        return entity;
    }
}
