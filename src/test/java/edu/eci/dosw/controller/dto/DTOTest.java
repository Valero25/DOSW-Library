package edu.eci.dosw.controller.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DTOTest {

    @Test
    void testBookDTO() {
        BookDTO dto = new BookDTO();
        dto.setId("1");
        dto.setTitle("Title");
        dto.setAuthor("Author");
        dto.setIsbn("123");
        dto.setAvailable(true);

        assertEquals("1", dto.getId());
        assertEquals("Title", dto.getTitle());
        assertEquals("Author", dto.getAuthor());
        assertEquals("123", dto.getIsbn());
        assertTrue(dto.isAvailable());
    }

    @Test
    void testUserDTO() {
        UserDTO dto = new UserDTO();
        dto.setId("1");
        dto.setName("Name");
        dto.setEmail("em");

        assertEquals("1", dto.getId());
        assertEquals("Name", dto.getName());
        assertEquals("em", dto.getEmail());
    }

    @Test
    void testLoanDTO() {
        LocalDate now = LocalDate.now();
        LoanDTO dto = new LoanDTO();
        dto.setId("1");
        dto.setBookId("b1");
        dto.setUserId("u1");
        dto.setLoanDate(now);
        dto.setReturnDate(now);
        dto.setReturned(true);

        assertEquals("1", dto.getId());
        assertEquals("b1", dto.getBookId());
        assertEquals("u1", dto.getUserId());
        assertEquals(now, dto.getLoanDate());
        assertEquals(now, dto.getReturnDate());
        assertTrue(dto.isReturned());
    }
}
