package edu.eci.dosw.util;

/**
 * Utility class providing common validation methods for the library system.
 */
public class ValidationUtil {

    private ValidationUtil() {
        // Utility class - prevent instantiation
    }

    /**
     * Validates that a string is not null or blank.
     *
     * @param value     the string to validate
     * @param fieldName the name of the field (used in the error message)
     * @throws IllegalArgumentException if the value is null or blank
     */
    public static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be null or blank.");
        }
    }

    /**
     * Validates that an object is not null.
     *
     * @param value     the object to validate
     * @param fieldName the name of the field (used in the error message)
     * @throws IllegalArgumentException if the value is null
     */
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null.");
        }
    }

    /**
     * Validates that a string matches a basic ISBN-13 format.
     *
     * @param isbn the ISBN to validate
     * @return true if the ISBN is valid
     */
    public static boolean isValidIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return false;
        }
        String cleaned = isbn.replaceAll("[- ]", "");
        return cleaned.matches("\\d{13}");
    }

    /**
     * Validates that an email address has a basic valid format.
     *
     * @param email the email to validate
     * @return true if the email format is valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }
}
