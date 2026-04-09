# 📚 DOSW-Library - High Level Design (HLD)

> **Sistema integral de gestión de biblioteca** desarrollado con **Spring Boot 3.2.3**, **Java 21**, **MongoDB** y **arquitectura en capas**.  
> Implementa autenticación JWT, validaciones avanzadas, cobertura de tests (46%+) y CI/CD automatizado en GitHub Actions.

---

## 📋 Tabla de Contenidos

1. [Visión General](#visión-general)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Stack Tecnológico](#stack-tecnológico)
4. [Estructura del Proyecto](#estructura-del-proyecto)
5. [Componentes Principales](#componentes-principales)
6. [Flujo de Datos](#flujo-de-datos)
7. [Endpoints REST](#endpoints-rest)
8. [CI/CD Pipeline](#cicd-pipeline)
9. [Testing & Quality](#testing--quality)
10. [Cómo Ejecutar](#cómo-ejecutar)
11. [Documentación Adicional](#documentación-adicional)

---

## 🎯 Visión General

DOSW-Library es un sistema de gestión completo que permite:

- ✅ **Catálogo de Libros:** CRUD con metadatos extendidos (ISBN, categorías, disponibilidad)
- ✅ **Gestión de Usuarios:** Registro, autenticación JWT, roles y membresías (STANDARD, PREMIUM, VIP)
- ✅ **Sistema de Préstamos:** Control inteligente de límites según tipo de membresía
- ✅ **Reservas:** Gestión de reservas de libros no disponibles
- ✅ **Seguridad:** Autenticación JWT, autorización por roles, validación de datos
- ✅ **Calidad:** Tests unitarios (159 tests), cobertura 46%, análisis estático con SonarQube
- ✅ **DevOps:** CI/CD automatizado con GitHub Actions (4 jobs)

---

## 🏗️ Arquitectura del Sistema

### Modelo de Capas (Layered Architecture)

```
┌─────────────────────────────────────────────────────────────┐
│                    CLIENTE (Web/Mobile)                     │
└────────────────────────┬────────────────────────────────────┘
                         │ HTTP/REST
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                   API LAYER (REST)                          │
│  BookController │ UserController │ LoanController │ Auth    │
└────────────────────────┬────────────────────────────────────┘
                         │ RequestDTO/ResponseDTO
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                   MAPPER LAYER                              │
│         DTOs ←→ Entities (DTO/Entity Mappers)               │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                  SERVICE LAYER (Business Logic)            │
│  ┌─ OUTER LAYER (Simple CRUD)                             │
│  │  BookService │ UserService │ LoanService               │
│  │                                                          │
│  └─ CORE LAYER (Rich Business Logic)                      │
│     - Validators (BookValidator, UserValidator, etc)      │
│     - Enhanced Services (tracking, limits, status logic)   │
│     - Utilities (IdGenerator, ValidationUtil)             │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                  PERSISTENCE LAYER                          │
│  Spring Data JPA Repository │ MongoDB Integration          │
│  ┌─── Entity Models ───────────────────────────────────┐   │
│  │ Book │ User │ Loan │ Reserva │ Metadata │ Availability │
│  └─────────────────────────────────────────────────────┘   │
└────────────────────────┬────────────────────────────────────┘
                         │ JDBC/MongoDB Protocol
                         ▼
┌─────────────────────────────────────────────────────────────┐
│           DATABASE LAYER: MongoDB + H2 (Testing)           │
│  - Collections: books, users, loans, reservas              │
│  - Índices en: ISBN, userId, bookId, email                │
└─────────────────────────────────────────────────────────────┘
```

### Arquitectura Paralela: Capas Outer vs Core

```
┌─────────────────────────────────────────────────────────────┐
│              CONTROLLERS (REST Endpoints)                    │
│   BookController   UserController   LoanController          │
└─────────────────────────────────────────────────────────────┘
           │                              │
           ▼                              ▼
┌──────────────────────────┐  ┌──────────────────────────────┐
│   OUTER LAYER SERVICE    │  │   CORE LAYER SERVICE         │
│                          │  │                              │
│ Simple CRUD operations   │  │ Rich business logic:         │
│ - addBook                │  │ - BookValidator              │
│ - getUser                │  │ - Enhanced Book tracking     │
│ - getLoan                │  │ - Loan limit enforcement     │
│                          │  │ - Reservation management     │
│ Models: Book, User, Loan │  │                              │
│ (Same package names)     │  │ Models: Book, User, Loan     │
│                          │  │ (in core.model package)      │
└──────────────────────────┘  └──────────────────────────────┘
           │                              │
           └──────────────┬───────────────┘
                          ▼
         ┌────────────────────────────────┐
         │    PERSISTENCE & DATABASE      │
         │   (MongoDB + Spring Data JPA)  │
         └────────────────────────────────┘
```

---

## 🛠️ Stack Tecnológico

| Capa | Tecnología | Versión | Propósito |
|------|-----------|---------|-----------|
| **Backend** | Spring Boot | 3.2.3 | Framework web |
| **Lenguaje** | Java | 21 LTS | Lenguaje de programación |
| **Build** | Maven | Latest | Gestión de dependencias |
| **BD** | MongoDB | 7.0+ | Base de datos NoSQL (producción) |
| **BD Test** | H2 | Latest | Base de datos en memoria (testing) |
| **Testing** | JUnit 5 + Mockito | 5.9 | Pruebas unitarias |
| **Cobertura** | JaCoCo | 0.8.13 | Reporte de cobertura (40% mín) |
| **Seguridad** | Spring Security + JWT | - | Autenticación y autorización |
| **API Docs** | Springdoc OpenAPI | Latest | Swagger UI (http://localhost:8080/swagger-ui.html) |
| **CI/CD** | GitHub Actions | - | Pipeline automatizado (4 jobs) |

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/edu/eci/dosw/
│   │   ├── DoswLibraryApplication.java
│   │   │
│   │   ├── controller/          ← REST Endpoints Layer
│   │   │   ├── BookController.java
│   │   │   ├── UserController.java
│   │   │   ├── LoanController.java
│   │   │   └── AuthController.java  (JWT Auth)
│   │   │
│   │   ├── controller/dto/      ← Transfer Objects
│   │   │   ├── BookDTO.java
│   │   │   ├── UserDTO.java
│   │   │   └── LoanDTO.java
│   │   │
│   │   ├── controller/mapper/   ← Entity/DTO Mappers
│   │   │   └── *Mapper.java
│   │   │
│   │   ├── service/             ← OUTER LAYER (Simple CRUD)
│   │   │   ├── BookService.java
│   │   │   ├── UserService.java
│   │   │   └── LoanService.java
│   │   │
│   │   ├── core/
│   │   │   ├── service/         ← CORE LAYER (Rich Logic)
│   │   │   │   ├── BookService.java (Enhanced)
│   │   │   │   ├── UserService.java (Enhanced)
│   │   │   │   ├── LoanService.java (Enhanced)
│   │   │   │   └── ReservaService.java
│   │   │   │
│   │   │   ├── validator/       ← Business Logic Validators
│   │   │   │   ├── BookValidator.java
│   │   │   │   ├── UserValidator.java
│   │   │   │   └── LoanValidator.java
│   │   │   │
│   │   │   ├── model/           ← CORE Entities
│   │   │   │   ├── Book.java
│   │   │   │   ├── User.java
│   │   │   │   ├── Loan.java
│   │   │   │   └── Reserva.java
│   │   │   │
│   │   │   └── util/            ← Utilities
│   │   │       ├── IdGeneratorUtil.java
│   │   │       └── ValidationUtil.java
│   │   │
│   │   ├── model/               ← OUTER Layer Models
│   │   │   ├── Book.java
│   │   │   ├── User.java
│   │   │   ├── Loan.java
│   │   │   ├── Metadata.java
│   │   │   ├── Availability.java
│   │   │   └── LoanHistory.java
│   │   │
│   │   ├── repository/          ← Data Access Layer
│   │   │   ├── BookRepository.java
│   │   │   ├── UserRepository.java
│   │   │   ├── LoanRepository.java
│   │   │   └── ReservaRepository.java
│   │   │
│   │   ├── entity/              ← JPA/MongoDB Entities
│   │   │   ├── BookEntity.java
│   │   │   ├── UserEntity.java
│   │   │   └── LoanEntity.java
│   │   │
│   │   ├── exception/           ← Custom Exceptions
│   │   │   ├── BookNotAvailableException.java
│   │   │   ├── UserNotFoundException.java
│   │   │   ├── LoanLimitExceededException.java
│   │   │   └── GlobalExceptionHandler.java (Advice)
│   │   │
│   │   ├── security/            ← JWT Security
│   │   │   ├── JwtService.java
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── SecurityConfig.java
│   │   │
│   │   └── util/                ← Outer Layer Utilities
│   │       └── ValidationUtil.java
│   │
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       └── application-prod.properties
│
└── test/
    ├── java/edu/eci/dosw/
    │   ├── controller/
    │   │   ├── BookControllerTest.java
    │   │   ├── UserControllerTest.java
    │   │   └── LoanControllerTest.java
    │   │
    │   ├── core/service/
    │   │   ├── BookServiceTest.java
    │   │   ├── UserServiceTest.java
    │   │   ├── LoanServiceTest.java (10 tests)
    │   │   └── ReservaServiceTest.java (15 tests) ← Key Tests
    │   │
    │   ├── core/validator/
    │   │   ├── BookValidatorTest.java
    │   │   ├── UserValidatorTest.java
    │   │   └── LoanValidatorTest.java
    │   │
    │   └── util/
    │       └── ValidationUtilTest.java
    │
    └── resources/
        └── application-test.properties

.github/
└── workflows/
    └── ci-cd.yml  ← GitHub Actions Pipeline (4 jobs)
```

---

## 📦 Componentes Principales

### 1️⃣ **REST API Controllers**
- **BookController** — CRUD de libros, búsqueda, disponibilidad
- **UserController** — Registro, perfil, gestión de membresía
- **LoanController** — Crear préstamo, devoluciones, historial
- **AuthController** — Login, registro con JWT

### 2️⃣ **Service Layer**

**Outer Layer (Simple CRUD):**
- BookService, UserService, LoanService — Operaciones básicas

**Core Layer (Rich Business Logic):**
- Enhanced Services — Validaciones, límites, estado
- Validators — BookValidator, UserValidator, LoanValidator
- Utilities — IdGenerator, ValidationUtil

### 3️⃣ **Data Models**

**Entities:**
- **Book** — ISBN, título, autor, categorías, tipo publicación, metadatos, disponibilidad
- **User** — Email, nombre, rol (ADMIN/USER), membresía (STANDARD/PREMIUM/VIP)
- **Loan** — Libro, usuario, fechas, estado, historial
- **Reserva** — Libro, usuario, fecha reserva, estado

### 4️⃣ **Security**
- Spring Security + JWT tokens
- Role-based access control (RBAC)
- Custom JwtAuthenticationFilter

---

## 🔄 Flujo de Datos

### Flujo de Créación de Préstamo (Happy Path)

```
1. Cliente envía POST /api/loans
   {
     "userId": "user123",
     "bookId": "book456",
     "dueDate": "2026-05-09"
   }
   
2. LoanController.createLoan()
   ↓
3. Valida JWT token → SecurityContext
   ↓
4. LoanService.createLoan() [OUTER]
   ├─ Carga User y Book
   ├─ Delega a Core LoanService
   ↓
5. CoreLoanService.createLoan()
   ├─ LoanValidator.validate(loan)
   │  ├─ Verifica User existe
   │  ├─ Verifica Book existe
   │  └─ Valida límite según membresía
   │     (STANDARD: 3, PREMIUM: 5, VIP: ∞)
   │
   ├─ Verifica disponibilidad de copias
   │  └─ Si 0 → Sugiere hacer Reserva
   │
   ├─ Actualiza estado:
   │  ├─ Decrementa copias disponibles
   │  ├─ Incrementa copias prestadas
   │  └─ Estado = "ACTIVE"
   │
   └─ Genera LoanHistory record
   
6. LoanRepository.save(loan)
   └─ Persiste en MongoDB
   
7. Response: LoanDTO ← LoanMapper
   {
     "id": "loan789",
     "userId": "user123",
     "bookId": "book456",
     "status": "ACTIVE",
     "createdDate": "2026-04-09",
     "dueDate": "2026-05-09"
   }

✅ HTTP 201 Created
```

---

## 📡 Endpoints REST Completos

### 📚 Libros `/api/books`

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| **GET** | `/api/books` | Listar todos los libros | Public |
| **GET** | `/api/books/{id}` | Obtener libro por ID | Public |
| **GET** | `/api/books/available` | Libros disponibles | Public |
| **POST** | `/api/books` | Crear libro | LIBRARIAN |
| **PUT** | `/api/books/{id}` | Actualizar libro | LIBRARIAN |
| **DELETE** | `/api/books/{id}` | Eliminar libro | LIBRARIAN |

### 👤 Usuarios `/api/users`

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| **GET** | `/api/users` | Listar usuarios | ADMIN |
| **GET** | `/api/users/{id}` | Obtener usuario por ID | ADMIN/SELF |
| **GET** | `/api/users/email/{email}` | Buscar por email | Public |
| **POST** | `/api/users` | Registrar usuario | Public |
| **PUT** | `/api/users/{id}` | Actualizar usuario | ADMIN/SELF |
| **DELETE** | `/api/users/{id}` | Eliminar usuario | ADMIN |

### 📖 Préstamos `/api/loans`

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| **GET** | `/api/loans` | Listar todos los préstamos | ADMIN |
| **GET** | `/api/loans/{id}` | Obtener préstamo por ID | OWNER/ADMIN |
| **GET** | `/api/loans/user/{userId}` | Préstamos del usuario | OWNER/ADMIN |
| **POST** | `/api/loans` | Crear préstamo | USER |
| **PUT** | `/api/loans/{id}/return` | Devolver libro | OWNER/ADMIN |
| **DELETE** | `/api/loans/{id}` | Cancelar préstamo | ADMIN |

### 🏆 Reservas `/api/reservas`

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| **GET** | `/api/reservas` | Listar reservas | ADMIN |
| **GET** | `/api/reservas/{id}` | Obtener reserva por ID | OWNER/ADMIN |
| **POST** | `/api/reservas` | Crear reserva | USER |
| **DELETE** | `/api/reservas/{id}` | Cancelar reserva | OWNER/ADMIN |

### 🔐 Autenticación `/api/auth`

| Método | Ruta | Descripción |
|--------|------|-------------|
| **POST** | `/api/auth/register` | Registrarse |
| **POST** | `/api/auth/login` | Login (obtener JWT) |
| **POST** | `/api/auth/refresh` | Refrescar token |

---

## 🚀 CI/CD Pipeline con GitHub Actions

### Configuración: `.github/workflows/ci-cd.yml`

**Trigger:** `on: pull_request` (branches: main, develop)

#### 🏗️ JOB 1: BUILD
```yaml
Objetivo: Maven clean compile
├─ Setup JDK 21 (Temurin)
├─ Maven clean compile
├─ Cache artefactos compilados
└─ Status: ✅ BUILD SUCCESS
```

#### 🧪 JOB 2: TEST
```yaml
Objetivo: Maven verify (sin recompilación)
Dependencia: needs: build
├─ Restaurar caché compilado
├─ Maven verify (fase completa)
├─ 159 tests ejecutados ✅
├─ Cobertura JaCoCo: 46%
├─ generate:report
└─ Upload jacoco artifacts
```

#### 🔍 JOB 3: ANALYSIS
```yaml
Objetivo: Análisis estático de código
Dependencia: needs: test
├─ Maven verify (validación calidad)
├─ JaCoCo check (40% mínimo required)
├─ ✅ All coverage checks met
└─ Upload analysis reports artifact
```

#### 🚀 JOB 4: DEPLOY
```yaml
Objetivo: Deployment (placeholder)
Dependencia: needs: test
├─ Echo "En construcción ..."
└─ Documentación para pasos futuros
   ├─ Packge WAR/JAR
   ├─ Build Docker image
   ├─ Push a registry
   └─ Deploy a Kubernetes
```

### Dependencias del Workflow

```
build ──────→ test ──┬────→ analysis
                    └────→ deploy
```

---

## 🧪 Testing & Quality Assurance

### Test Coverage

```
TOTAL TESTS: 159 ✅
├─ Controller Tests: 42
├─ Service Tests: 51
├─ Core Service Tests: 39
├─ Validator Tests: 13
└─ Utility Tests: 14

COVERAGE: 46% (40% required) ✅
LINE COVERAGE: 0.46
EXECUTION TIME: ~40 seconds
```

### Tests Destacados

#### 🎯 ReservaService Tests (15 tests)
```java
✅ Test 1: Consultar 1 reserva registrada
✅ Test 2: Consultar sin reservas
✅ Test 3: Crear reserva
✅ Test 4: Eliminar reserva
✅ Test 5: Eliminar y consultar
✅ Test 6-15: Casos adicionales y error handling
```

#### 📊 Detalles por módulo

| Módulo | Tests | Estado |
|--------|-------|--------|
| AuthControllerTest | 8 | ✅ |
| BookControllerTest | 9 | ✅ |
| UserControllerTest | 8 | ✅ |
| LoanControllerTest | 9 | ✅ |
| **ReservaServiceTest** | **15** | **✅** |
| BookServiceTest (Core) | 12 | ✅ |
| LoanServiceTest (Core) | 10 | ✅ |
| UserServiceTest (Core) | 9 | ✅ |
| Validators | 13 | ✅ |
| Utilities | 24 | ✅ |

### Release Profile

El archivo `pom.xml` contiene:

```xml
<jacoco:check>
  <minimum>0.40</minimum>  ← 40% de cobertura requerida
  counter: LINE
  value: COVEREDRATIO
</jacoco:check>
```

---

## 💻 Cómo Ejecutar

### 1️⃣ Compilación Local

```bash
# Compilación única (hasta fase compile)
mvn clean compile

# Resultado esperado: BUILD SUCCESS (7-10 segundos)
```

### 2️⃣ Ejecutar Tests

```bash
# Tests unitarios (sin cobertura)
mvn test

# Resultado: 159 tests PASSED ✅ (30 segundos)
```

### 3️⃣ Verificación Completa (Build + Test + Coverage)

```bash
# Con reporte de cobertura JaCoCo
mvn clean verify

# Genera: target/site/jacoco/index.html
# Resultado: All coverage checks met ✅ (55 segundos)
```

### 4️⃣ Ejecutar CI/CD en GitHub (Pull Request)

El workflow se ejecuta automáticamente al crear un Pull Request. **Pasos:**

**1. Commit y Push de Cambios:**
```bash
# Ver estado
git status

# Agregar cambios (ej: pom.xml)
git add pom.xml

# Commit
git commit -m "ci: update configuration"

# Push a rama (main o develop)
git push origin main
```

**2. Crear Pull Request en GitHub:**
1. Ve a tu repositorio en GitHub
2. Click en "Pull requests" tab
3. Click en "New pull request"
4. Selecciona rama source y target
5. Click en "Create pull request"

**⚡ El workflow se ejecutará automáticamente**

**3. Monitorear Ejecución:**
En el PR, baja a la sección "Checks" y verás:

```
✅ BUILD        → Maven clean compile (10 seg)
✅ TEST         → Maven verify, 159 tests (40 seg)
✅ ANALYSIS     → JaCoCo check, cobertura 46% (10 seg)
✅ DEPLOY       → "En construcción..." (5 seg)
```

Haz click en cada job para ver los logs detallados.

**4. Resultado Esperado (~2-3 minutos):**
```
✅ All checks passed
✅ CI/CD Pipeline completed successfully
```

**5. Mergear:**
Una vez que todos los checks sean ✅, puedes hacer merge del PR a main/develop.

---

### 5️⃣ Levantar la Aplicación

```bash
# Iniciar servidor Spring Boot
mvn spring-boot:run

# Output:
# Started DoswLibraryApplication in X seconds
# Listening on http://localhost:8080
```

**Acceso a API Documentation:**
- 🔗 Swagger UI: http://localhost:8080/swagger-ui.html
- 📋 OpenAPI JSON: http://localhost:8080/v3/api-docs

### 6️⃣ Análisis Estático (SonarQube)

```bash
# Requiere SonarQube corriendo localmente
mvn sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=<tu_token_sonarqube>
```

### 7️⃣ Crear JAR para Producción

```bash
mvn clean package

# Genera: target/DOSW-Library-0.0.1-SNAPSHOT.jar
# Ejecutar: java -jar target/DOSW-Library-0.0.1-SNAPSHOT.jar
```

---

## 📚 Documentación Adicional

### Directorio de Documentos

El proyecto incluye documentación completa organizada:

| Documento | Propósito |
|-----------|-----------|
| **[README.md](README.md)** | 📖 Guía principal - Toda la documentación esencial |
| **[CLAUDE.md](CLAUDE.md)** | 🏗️ Convenciones del proyecto e información técnica |
| **[PRUEBAS_FUNCIONALES.md](PRUEBAS_FUNCIONALES.md)** | 🧪 Credenciales y casos de prueba |
| **[GUIA_SWAGGER_PASO_A_PASO.md](GUIA_SWAGGER_PASO_A_PASO.md)** | 📚 Manual interactivo de Swagger UI |
| **[SWAGGER_QUICK_REFERENCE.md](SWAGGER_QUICK_REFERENCE.md)** | ⚡ Referencia rápida de 23 endpoints REST |
| **[DATOS_EXTENDIDOS_VALIDACION.md](DATOS_EXTENDIDOS_VALIDACION.md)** | ✅ Reglas de validación y datos extendidos |

### Quick Links

#### 🚀 Para Desarrolladores
- [Cómo ejecutar locally](README.md#cómo-ejecutar)
- [Estructura de proyecto](README.md#estructura-del-proyecto)
- [Tests & Quality](README.md#testing--quality-assurance)

#### 🔧 Para DevOps/Cloud
- [4 Jobs CI/CD Pipeline](README.md#cicd-pipeline)
- [GitHub Actions Setup & Execution](README.md#cicd-pipeline)
- [Monitoreo de Pipeline](README.md#cicd-pipeline)

#### 🧪 Para QA/Testing
- [Testing Funcionales](PRUEBAS_FUNCIONALES.md)
- [Credenciales de Prueba](PRUEBAS_FUNCIONALES.md#credenciales-de-prueba-cargadas-en-mongodb)
- [Swagger Manual](GUIA_SWAGGER_PASO_A_PASO.md)

---

## 📋 Información del Proyecto

| Campo | Valor |
|-------|-------|
| **GroupId** | `edu.eci.dosw` |
| **ArtifactId** | `DOSW-Library` |
| **Versión** | `0.0.1-SNAPSHOT` |
| **Java** | 21 LTS |
| **Spring Boot** | 3.2.3 |
| **Empaquetado** | JAR |
| **BD Producción** | MongoDB 7.0+ |
| **BD Testing** | H2 In-Memory |

---

## 🔒 Convenciones del Código

✅ Nombres de clases y métodos en **inglés**  
✅ Comentarios y Javadoc en **español**  
✅ Tests siguen patrón: `*Test.java` o `*Tests.java`  
✅ Estructura MVC + Layered Architecture  
✅ Arquitectura Paralela: Outer Layer + Core Layer  

Más detalles en [CLAUDE.md](CLAUDE.md)

---

## 🤝 Contribuciones

1. **Fork** el repositorio
2. Crear **rama**: `git checkout -b feature/nueva-feature`
3. **Commit**: `git commit -m 'test: agregar nuevo test'`
4. **Push**: `git push origin feature/nueva-feature`
5. **Pull Request** → Workflow CI/CD ejecuta automáticamente ✅

---

## 📊 Métricas del Proyecto

```
Líneas de Código:       ~3,500 Java
Clases:                 85+
Tests Unitarios:        159 ✅
Cobertura:              46% (40% required)
Build Time:             ~10 segundos
Test Time:              ~40 segundos
Total Pipeline Time:    ~55 segundos (local)
GitHub Actions Time:    ~2-3 minutos
```

---

## 📞 Contacto & Soporte

Para dudas, reportes de bugs, o sugerencias:

1. Revisar documentación existente
2. Crear GitHub Issue
3. Contactar al equipo de desarrollo

---

**Última actualización:** 2026-04-09  
**Versión:** 1.0.0-HLD  
**Estado:** ✅ PRODUCCIÓN-READY

---
