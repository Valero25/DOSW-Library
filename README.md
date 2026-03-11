# DOSW-Library

> Proyecto de gestión de biblioteca desarrollado con **Spring Boot** y **Maven**, siguiendo el patrón **MVC**.

## Información del proyecto

| Campo       | Valor                     |
|-------------|---------------------------|
| GroupId     | `edu.eci.dosw`            |
| ArtifactId  | `DOSW-Library`            |
| Java        | 17                        |
| Spring Boot | 3.2.3                     |
| Empaquetado | JAR                       |

## Tecnologías y herramientas

- **Spring Boot 3.2.x** — Framework principal
- **JUnit 5** — Pruebas unitarias
- **JaCoCo** — Cobertura de código (mínimo 80 % de líneas)
- **SonarQube** — Análisis de calidad de código
- **Maven** — Gestión de dependencias y build

## Estructura del proyecto

```
src/
├── main/
│   ├── java/edu/eci/dosw/
│   │   ├── DoswLibraryApplication.java
│   │   ├── controller/
│   │   │   ├── BookController.java
│   │   │   ├── UserController.java
│   │   │   └── LoanController.java
│   │   ├── service/
│   │   │   └── LibraryService.java
│   │   ├── model/
│   │   │   ├── Book.java
│   │   │   ├── User.java
│   │   │   └── Loan.java
│   │   ├── util/
│   │   │   └── ValidationUtil.java
│   │   └── exception/
│   │       └── BookNotAvailableException.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/edu/eci/dosw/
        ├── DoswLibraryApplicationTests.java
        ├── service/
        │   └── LibraryServiceTest.java
        └── util/
            └── ValidationUtilTest.java
```

## Endpoints REST

### Libros — `/api/books`
| Método | Ruta            | Descripción              |
|--------|-----------------|--------------------------|
| GET    | `/api/books`    | Listar todos los libros  |
| GET    | `/api/books/{id}` | Obtener libro por ID   |
| POST   | `/api/books`    | Agregar un libro         |
| DELETE | `/api/books/{id}` | Eliminar un libro      |

### Usuarios — `/api/users`
| Método | Ruta             | Descripción                |
|--------|------------------|----------------------------|
| GET    | `/api/users`     | Listar todos los usuarios  |
| GET    | `/api/users/{id}`| Obtener usuario por ID     |
| POST   | `/api/users`     | Registrar un usuario       |

### Préstamos — `/api/loans`
| Método | Ruta                       | Descripción              |
|--------|----------------------------|--------------------------|
| GET    | `/api/loans`               | Listar todos los préstamos |
| POST   | `/api/loans`               | Crear un préstamo        |
| PUT    | `/api/loans/{id}/return`   | Devolver un libro        |

## Cómo ejecutar

```bash
# Compilar y ejecutar
mvn spring-boot:run

# Ejecutar pruebas
mvn test

# Generar reporte de cobertura (JaCoCo)
mvn verify

# Análisis SonarQube (requiere servidor SonarQube)
mvn sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=<token>
```

El reporte de cobertura JaCoCo se genera en `target/site/jacoco/index.html`.

## Cobertura mínima

JaCoCo está configurado para fallar el build si la cobertura de líneas es inferior al **80 %**.
