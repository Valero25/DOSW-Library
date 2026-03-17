package edu.eci.dosw.controller.mapper;

import edu.eci.dosw.controller.dto.LoanDTO;
import edu.eci.dosw.core.model.Book;
import edu.eci.dosw.core.model.Loan;
import edu.eci.dosw.core.model.User;

/**
 * Clase responsable de convertir entre la entidad {@link Loan} y su correspondiente
 * objeto de transferencia de datos {@link LoanDTO}.
 * Desacopla la representación interna del dominio de la representación expuesta por la API REST.
 */
public class LoanMapper {

    private LoanMapper() {
        // Clase utilitaria — no se permite instanciar
    }

    /**
     * Convierte una entidad {@link Loan} en su representación {@link LoanDTO}.
     * Los objetos {@link Book} y {@link User} anidados se representan solo por sus identificadores.
     *
     * @param loan entidad préstamo a convertir; no debe ser nula
     * @return DTO con los datos del préstamo
     */
    public static LoanDTO toDTO(Loan loan) {
        return new LoanDTO(
                loan.getId(),
                loan.getBook().getId(),
                loan.getUser().getId(),
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.isReturned()
        );
    }

    /**
     * Convierte un {@link LoanDTO} en la entidad de dominio {@link Loan},
     * utilizando las entidades de libro y usuario ya resueltas.
     *
     * @param dto  DTO del préstamo a convertir; no debe ser nulo
     * @param book entidad libro asociada al préstamo
     * @param user entidad usuario que realizó el préstamo
     * @return entidad préstamo correspondiente al DTO
     */
    public static Loan toModel(LoanDTO dto, Book book, User user) {
        Loan loan = new Loan(dto.getId(), book, user, dto.getLoanDate());
        loan.setReturned(dto.isReturned());
        loan.setReturnDate(dto.getReturnDate());
        return loan;
    }
}
