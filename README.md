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

### Ejecución de las funcionalidades de su API

Para levantar el servidor y probar los Endpoints REST (por ejemplo herramientas como Postman o cURL):

```bash
mvn spring-boot:run
```
La aplicación se ejecutará en http://localhost:8080.

### Ejecución de las pruebas de los servicios

Para correr todas las pruebas unitarias y verificar los escenarios exitosos y de error del sistema:

```bash
mvn test
```

### Cobertura y análisis estático

Para ejecutar los reportes de cobertura (con JaCoCo) garantizando que se cumpla el umbral del **80%**:

```bash
mvn verify
```
*El reporte interactivo de cobertura se genera en la ruta `target/site/jacoco/index.html`.*

Para el análisis estático de código mediante SonarQube (se debe tener un servidor SonarQube en ejecución local o remoto):

```bash
mvn sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=<tu_token>
```
