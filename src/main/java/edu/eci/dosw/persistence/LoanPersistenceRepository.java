package edu.eci.dosw.persistence;

import edu.eci.dosw.model.Loan;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz abstracta para la persistencia de préstamos.
 */
public interface LoanPersistenceRepository {

    Loan save(Loan loan);

    Optional<Loan> findById(String id);

    List<Loan> findAll();

    List<Loan> findByUserId(String userId);

    void deleteById(String id);
}
