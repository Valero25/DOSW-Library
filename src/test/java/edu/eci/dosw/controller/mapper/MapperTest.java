package edu.eci.dosw.controller.mapper;

import edu.eci.dosw.controller.dto.BookDTO;
import edu.eci.dosw.controller.dto.LoanDTO;
import edu.eci.dosw.controller.dto.UserDTO;
import edu.eci.dosw.core.model.Book;
import edu.eci.dosw.core.model.Loan;
import edu.eci.dosw.core.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {

    private User createUser(String id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    @Test
    void testBookMapper() {
        Book book = new Book("1", "T", "A", "I");
        book.setAvailable(true);
        BookDTO dto = BookMapper.toDTO(book);
        assertEquals("1", dto.getId());

        Book model = BookMapper.toModel(dto);
        assertEquals("1", model.getId());
        assertEquals("T", model.getTitle());
    }

    @Test
    void testUserMapper() {
        User user = createUser("1", "N", "E");
        UserDTO dto = UserMapper.toDTO(user);
        assertEquals("1", dto.getId());

        User model = UserMapper.toModel(dto);
        assertEquals("1", model.getId());
        assertEquals("N", model.getName());
    }

    @Test
    void testLoanMapper() {
        Book book = new Book("b1", "T", "A", "I");
        User user = createUser("u1", "N", "E");
        LocalDate now = LocalDate.now();
        Loan loan = new Loan("l1", book, user, now);
        loan.setReturned(true);
        loan.setReturnDate(now);

        LoanDTO dto = LoanMapper.toDTO(loan);
        assertEquals("l1", dto.getId());
        assertEquals("b1", dto.getBookId());
        assertEquals("u1", dto.getUserId());

        Loan model = LoanMapper.toModel(dto, book, user);
        assertEquals("l1", model.getId());
        assertTrue(model.isReturned());
        assertEquals(now, model.getReturnDate());
    }
}
