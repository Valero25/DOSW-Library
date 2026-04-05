package edu.eci.dosw.persistence.relational;

import edu.eci.dosw.model.Loan;
import edu.eci.dosw.model.LoanHistory;
import edu.eci.dosw.persistence.LoanPersistenceRepository;
import edu.eci.dosw.persistence.relational.entity.BookEntity;
import edu.eci.dosw.persistence.relational.entity.LoanEntity;
import edu.eci.dosw.persistence.relational.entity.LoanHistoryEntity;
import edu.eci.dosw.persistence.relational.entity.UserEntity;
import edu.eci.dosw.persistence.relational.mapper.LoanEntityMapper;
import edu.eci.dosw.persistence.relational.repository.JpaBookRepository;
import edu.eci.dosw.persistence.relational.repository.JpaLoanRepository;
import edu.eci.dosw.persistence.relational.repository.JpaUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("relational")
public class LoanRepositoryJpaImpl implements LoanPersistenceRepository {

    private final JpaLoanRepository loanRepository;
    private final JpaBookRepository bookRepository;
    private final JpaUserRepository userRepository;

    public LoanRepositoryJpaImpl(JpaLoanRepository loanRepository,
                                 JpaBookRepository bookRepository,
                                 JpaUserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Loan save(Loan loan) {
        LoanEntity entity;

        // Si el préstamo ya existe, actualizarlo
        if (loan.getId() != null) {
            Optional<LoanEntity> existing = loanRepository.findById(loan.getId());
            if (existing.isPresent()) {
                entity = existing.get();
                entity.setLoanDate(loan.getLoanDate());
                entity.setReturnDate(loan.getReturnDate());
                entity.setReturned(loan.isReturned());

                // Actualizar historial
                if (loan.getLoanHistory() != null) {
                    entity.getLoanHistory().clear();
                    for (LoanHistory h : loan.getLoanHistory()) {
                        LoanHistoryEntity he = new LoanHistoryEntity();
                        he.setStatus(h.getStatus());
                        he.setDate(h.getDate());
                        he.setLoan(entity);
                        entity.getLoanHistory().add(he);
                    }
                }

                return LoanEntityMapper.toDomain(loanRepository.save(entity));
            }
        }

        // Crear nuevo préstamo
        entity = new LoanEntity();
        entity.setLoanDate(loan.getLoanDate());
        entity.setReturnDate(loan.getReturnDate());
        entity.setReturned(loan.isReturned());

        // Resolver referencias de libro y usuario
        if (loan.getBook() != null && loan.getBook().getId() != null) {
            BookEntity bookEntity = bookRepository.findById(loan.getBook().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + loan.getBook().getId()));
            entity.setBook(bookEntity);
        }
        if (loan.getUser() != null && loan.getUser().getId() != null) {
            UserEntity userEntity = userRepository.findById(loan.getUser().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + loan.getUser().getId()));
            entity.setUser(userEntity);
        }

        // Historial
        if (loan.getLoanHistory() != null) {
            List<LoanHistoryEntity> historyEntities = new ArrayList<>();
            for (LoanHistory h : loan.getLoanHistory()) {
                LoanHistoryEntity he = new LoanHistoryEntity();
                he.setStatus(h.getStatus());
                he.setDate(h.getDate());
                he.setLoan(entity);
                historyEntities.add(he);
            }
            entity.setLoanHistory(historyEntities);
        }

        return LoanEntityMapper.toDomain(loanRepository.save(entity));
    }

    @Override
    public Optional<Loan> findById(String id) {
        return loanRepository.findById(id).map(LoanEntityMapper::toDomain);
    }

    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll().stream()
                .map(LoanEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(LoanEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        loanRepository.deleteById(id);
    }
}
