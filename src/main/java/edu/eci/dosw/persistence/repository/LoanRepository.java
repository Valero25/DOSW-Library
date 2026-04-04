package edu.eci.dosw.persistence.repository;

import edu.eci.dosw.persistence.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, String> {

    List<LoanEntity> findByUserId(String userId);

    List<LoanEntity> findByBookIdAndReturnedFalse(String bookId);

    List<LoanEntity> findByUserIdAndReturnedFalse(String userId);
}
