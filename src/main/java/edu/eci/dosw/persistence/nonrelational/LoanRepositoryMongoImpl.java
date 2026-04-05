package edu.eci.dosw.persistence.nonrelational;

import edu.eci.dosw.model.Loan;
import edu.eci.dosw.persistence.LoanPersistenceRepository;
import edu.eci.dosw.persistence.nonrelational.document.LoanDocument;
import edu.eci.dosw.persistence.nonrelational.mapper.BookDocumentMapper;
import edu.eci.dosw.persistence.nonrelational.mapper.LoanDocumentMapper;
import edu.eci.dosw.persistence.nonrelational.mapper.UserDocumentMapper;
import edu.eci.dosw.persistence.nonrelational.repository.MongoBookRepository;
import edu.eci.dosw.persistence.nonrelational.repository.MongoLoanRepository;
import edu.eci.dosw.persistence.nonrelational.repository.MongoUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
public class LoanRepositoryMongoImpl implements LoanPersistenceRepository {

    private final MongoLoanRepository loanRepository;
    private final MongoBookRepository bookRepository;
    private final MongoUserRepository userRepository;

    public LoanRepositoryMongoImpl(MongoLoanRepository loanRepository,
                                   MongoBookRepository bookRepository,
                                   MongoUserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Loan save(Loan loan) {
        String bookId = loan.getBook() != null ? loan.getBook().getId() : null;
        String userId = loan.getUser() != null ? loan.getUser().getId() : null;
        LoanDocument saved = loanRepository.save(LoanDocumentMapper.toDocument(loan, bookId, userId));
        return toDomainWithReferences(saved);
    }

    @Override
    public Optional<Loan> findById(String id) {
        return loanRepository.findById(id).map(this::toDomainWithReferences);
    }

    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll().stream()
                .map(this::toDomainWithReferences)
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(this::toDomainWithReferences)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        loanRepository.deleteById(id);
    }

    private Loan toDomainWithReferences(LoanDocument doc) {
        Loan loan = LoanDocumentMapper.toDomain(doc);
        if (doc.getBookId() != null) {
            bookRepository.findById(doc.getBookId())
                    .ifPresent(bookDoc -> loan.setBook(BookDocumentMapper.toDomain(bookDoc)));
        }
        if (doc.getUserId() != null) {
            userRepository.findById(doc.getUserId())
                    .ifPresent(userDoc -> loan.setUser(UserDocumentMapper.toDomain(userDoc)));
        }
        return loan;
    }
}
