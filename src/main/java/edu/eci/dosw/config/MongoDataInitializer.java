package edu.eci.dosw.config;

import edu.eci.dosw.model.Availability;
import edu.eci.dosw.model.Book;
import edu.eci.dosw.model.Metadata;
import edu.eci.dosw.model.User;
import edu.eci.dosw.model.enums.MembershipType;
import edu.eci.dosw.model.enums.PublicationType;
import edu.eci.dosw.persistence.BookPersistenceRepository;
import edu.eci.dosw.persistence.UserPersistenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Inicializador de datos para MongoDB.
 * Se ejecuta automáticamente al startup si el perfil "mongo" está activo.
 */
@Configuration
@Profile("mongo")
@Slf4j
public class MongoDataInitializer {

    @Bean
    public CommandLineRunner initializeMongoData(BookPersistenceRepository bookRepository,
                                                  UserPersistenceRepository userRepository,
                                                  PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                // Verificar si ya hay datos
                if (bookRepository.findAll().isEmpty()) {
                    log.info("📚 Inicializando datos de prueba en MongoDB...");
                    
                    // Crear usuarios de prueba
                    createTestUsers(userRepository, passwordEncoder);
                    
                    // Crear libros de prueba
                    createTestBooks(bookRepository);
                    
                    log.info("✅ Datos de prueba cargados exitosamente en MongoDB");
                } else {
                    log.info("ℹ️  MongoDB ya contiene datos. Saltando inicialización.");
                }
            } catch (Exception e) {
                log.error("❌ Error durante la inicialización de datos: ", e);
            }
        };
    }

    private void createTestUsers(UserPersistenceRepository userRepository, PasswordEncoder passwordEncoder) {
        // Usuario 1: Administrador/Bibliotecario
        User admin = new User();
        admin.setId("user-admin-001");
        admin.setName("Carlos Administrador");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@dosw-library.com");
        admin.setRole("LIBRARIAN");
        admin.setMembershipType(MembershipType.PLATINUM);
        admin.setDateAddedAsUser(LocalDate.now());
        
        // Usuario 2: Usuario Regular VIP
        User userVip = new User();
        userVip.setId("user-vip-001");
        userVip.setName("Juan Pérez García");
        userVip.setUsername("juan.perez");
        userVip.setPassword(passwordEncoder.encode("juan123456"));
        userVip.setEmail("juan.perez@email.com");
        userVip.setRole("USER");
        userVip.setMembershipType(MembershipType.VIP);
        userVip.setDateAddedAsUser(LocalDate.now().minusDays(30));
        
        // Usuario 3: Usuario Regular Premium
        User userPremium = new User();
        userPremium.setId("user-premium-001");
        userPremium.setName("María López Rodríguez");
        userPremium.setUsername("maria.lopez");
        userPremium.setPassword(passwordEncoder.encode("maria789012"));
        userPremium.setEmail("maria.lopez@email.com");
        userPremium.setRole("USER");
        userPremium.setMembershipType(MembershipType.STANDARD);
        userPremium.setDateAddedAsUser(LocalDate.now().minusDays(15));
        
        // Usuario 4: Usuario Regular Standard
        User userStandard = new User();
        userStandard.setId("user-standard-001");
        userStandard.setName("Pedro Sánchez López");
        userStandard.setUsername("pedro.sanchez");
        userStandard.setPassword(passwordEncoder.encode("pedro345678"));
        userStandard.setEmail("pedro.sanchez@email.com");
        userStandard.setRole("USER");
        userStandard.setMembershipType(MembershipType.STANDARD);
        userStandard.setDateAddedAsUser(LocalDate.now().minusDays(5));
        
        userRepository.save(admin);
        userRepository.save(userVip);
        userRepository.save(userPremium);
        userRepository.save(userStandard);
        
        log.info("✅ 4 usuarios de prueba creados en MongoDB");
    }

    private void createTestBooks(BookPersistenceRepository bookRepository) {
        // Libro 1: Clean Code
        Book book1 = new Book();
        book1.setId("book-001");
        book1.setTitle("Clean Code");
        book1.setAuthor("Robert C. Martin");
        book1.setIsbn("978-0132350884");
        book1.setPublicationType(PublicationType.LIBRO);
        book1.setPublicationDate(LocalDate.of(2008, 8, 1));
        book1.setAvailable(true);
        
        Availability avail1 = new Availability();
        avail1.setStatus("Disponible");
        avail1.setTotalCopies(5);
        avail1.setAvailableCopies(4);
        avail1.setLoanedCopies(1);
        book1.setAvailability(avail1);
        
        Metadata meta1 = new Metadata();
        meta1.setPages(464);
        meta1.setLanguage("English");
        meta1.setPublisher("Prentice Hall");
        book1.setMetadata(meta1);
        
        book1.setCategories(new ArrayList<>());
        book1.getCategories().add("Programming");
        book1.getCategories().add("Software Engineering");
        
        book1.setDateAddedToCatalog(LocalDate.now().minusDays(90));
        
        // Libro 2: The Pragmatic Programmer
        Book book2 = new Book();
        book2.setId("book-002");
        book2.setTitle("The Pragmatic Programmer");
        book2.setAuthor("David Thomas, Andrew Hunt");
        book2.setIsbn("978-0201616224");
        book2.setPublicationType(PublicationType.LIBRO);
        book2.setPublicationDate(LocalDate.of(1999, 10, 1));
        book2.setAvailable(true);
        
        Availability avail2 = new Availability();
        avail2.setStatus("Disponible");
        avail2.setTotalCopies(3);
        avail2.setAvailableCopies(3);
        avail2.setLoanedCopies(0);
        book2.setAvailability(avail2);
        
        Metadata meta2 = new Metadata();
        meta2.setPages(352);
        meta2.setLanguage("English");
        meta2.setPublisher("Addison-Wesley");
        book2.setMetadata(meta2);
        
        book2.setCategories(new ArrayList<>());
        book2.getCategories().add("Programming");
        book2.getCategories().add("Best Practices");
        
        book2.setDateAddedToCatalog(LocalDate.now().minusDays(60));
        
        // Libro 3: Design Patterns
        Book book3 = new Book();
        book3.setId("book-003");
        book3.setTitle("Design Patterns: Elements of Reusable Object-Oriented Software");
        book3.setAuthor("Gang of Four");
        book3.setIsbn("978-0201633610");
        book3.setPublicationType(PublicationType.LIBRO);
        book3.setPublicationDate(LocalDate.of(1994, 11, 1));
        book3.setAvailable(true);
        
        Availability avail3 = new Availability();
        avail3.setStatus("Disponible");
        avail3.setTotalCopies(4);
        avail3.setAvailableCopies(2);
        avail3.setLoanedCopies(2);
        book3.setAvailability(avail3);
        
        Metadata meta3 = new Metadata();
        meta3.setPages(395);
        meta3.setLanguage("English");
        meta3.setPublisher("Addison-Wesley");
        book3.setMetadata(meta3);
        
        book3.setCategories(new ArrayList<>());
        book3.getCategories().add("Design Patterns");
        book3.getCategories().add("Software Architecture");
        
        book3.setDateAddedToCatalog(LocalDate.now().minusDays(120));
        
        // Libro 4: Spring in Action
        Book book4 = new Book();
        book4.setId("book-004");
        book4.setTitle("Spring in Action");
        book4.setAuthor("Craig Walls");
        book4.setIsbn("978-1617299032");
        book4.setPublicationType(PublicationType.LIBRO);
        book4.setPublicationDate(LocalDate.of(2018, 10, 1));
        book4.setAvailable(true);
        
        Availability avail4 = new Availability();
        avail4.setStatus("Disponible");
        avail4.setTotalCopies(6);
        avail4.setAvailableCopies(5);
        avail4.setLoanedCopies(1);
        book4.setAvailability(avail4);
        
        Metadata meta4 = new Metadata();
        meta4.setPages(672);
        meta4.setLanguage("English");
        meta4.setPublisher("Manning");
        book4.setMetadata(meta4);
        
        book4.setCategories(new ArrayList<>());
        book4.getCategories().add("Spring Framework");
        book4.getCategories().add("Java");
        
        book4.setDateAddedToCatalog(LocalDate.now().minusDays(45));
        
        // Libro 5: Effective Java
        Book book5 = new Book();
        book5.setId("book-005");
        book5.setTitle("Effective Java");
        book5.setAuthor("Joshua Bloch");
        book5.setIsbn("978-0134685991");
        book5.setPublicationType(PublicationType.LIBRO);
        book5.setPublicationDate(LocalDate.of(2018, 12, 1));
        book5.setAvailable(true);
        
        Availability avail5 = new Availability();
        avail5.setStatus("Disponible");
        avail5.setTotalCopies(7);
        avail5.setAvailableCopies(6);
        avail5.setLoanedCopies(1);
        book5.setAvailability(avail5);
        
        Metadata meta5 = new Metadata();
        meta5.setPages(416);
        meta5.setLanguage("English");
        meta5.setPublisher("Addison-Wesley");
        book5.setMetadata(meta5);
        
        book5.setCategories(new ArrayList<>());
        book5.getCategories().add("Java");
        book5.getCategories().add("Best Practices");
        
        book5.setDateAddedToCatalog(LocalDate.now().minusDays(30));
        
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
        bookRepository.save(book5);
        
        log.info("✅ 5 libros de prueba creados en MongoDB");
    }
}
