package edu.eci.dosw.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void validateNotBlank_ShouldPass_WhenValueIsValid() {
        assertDoesNotThrow(() -> ValidationUtil.validateNotBlank("hello", "field"));
    }

    @Test
    void validateNotBlank_ShouldThrow_WhenValueIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateNotBlank(null, "field"));
    }

    @Test
    void validateNotBlank_ShouldThrow_WhenValueIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateNotBlank("  ", "field"));
    }

    @Test
    void validateNotNull_ShouldPass_WhenValueIsNotNull() {
        assertDoesNotThrow(() -> ValidationUtil.validateNotNull(new Object(), "obj"));
    }

    @Test
    void validateNotNull_ShouldThrow_WhenValueIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateNotNull(null, "obj"));
    }

    @Test
    void isValidIsbn_ShouldReturnTrue_ForValid13DigitIsbn() {
        assertTrue(ValidationUtil.isValidIsbn("9780132350884"));
    }

    @Test
    void isValidIsbn_ShouldReturnTrue_ForIsbnWithHyphens() {
        assertTrue(ValidationUtil.isValidIsbn("978-0-13-235088-4"));
    }

    @Test
    void isValidIsbn_ShouldReturnFalse_ForShortIsbn() {
        assertFalse(ValidationUtil.isValidIsbn("12345"));
    }

    @Test
    void isValidIsbn_ShouldReturnFalse_ForNull() {
        assertFalse(ValidationUtil.isValidIsbn(null));
    }

    @Test
    void isValidEmail_ShouldReturnTrue_ForValidEmail() {
        assertTrue(ValidationUtil.isValidEmail("user@eci.edu.co"));
    }

    @Test
    void isValidEmail_ShouldReturnFalse_ForInvalidEmail() {
        assertFalse(ValidationUtil.isValidEmail("not-an-email"));
    }

    @Test
    void isValidEmail_ShouldReturnFalse_ForNull() {
        assertFalse(ValidationUtil.isValidEmail(null));
    }
}
