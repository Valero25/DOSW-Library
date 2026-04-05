package edu.eci.dosw.persistence.nonrelational.repository;

import edu.eci.dosw.persistence.nonrelational.document.LoanDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoLoanRepository extends MongoRepository<LoanDocument, String> {

    List<LoanDocument> findByUserId(String userId);

    List<LoanDocument> findByBookIdAndReturnedFalse(String bookId);

    List<LoanDocument> findByUserIdAndReturnedFalse(String userId);
}
