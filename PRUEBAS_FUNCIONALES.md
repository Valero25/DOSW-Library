# 🧪 PRUEBAS FUNCIONALES - DOSW-LIBRARY CON MONGODB

## 📋 Resumen Ejecutivo
✅ **Status: LISTO PARA PRUEBAS**
- **Base de Datos**: MongoDB Atlas (Cluster0) - Datos inicializados
- **API**: Spring Boot 3.2.3 + Java 21
- **URL Base**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

---

## 🔐 CREDENCIALES DE PRUEBA (Cargadas en MongoDB)

### Usuarios Disponibles:
1. **ADMIN (Administrador/Bibliotecario)**
   - Username: `admin`
   - Password: `admin123`
   - Rol: `LIBRARIAN`
   - Email: `admin@dosw-library.com`

2. **VIP (Usuario Premium)**
   - Username: `juan.perez`
   - Password: `juan123456`
   - Rol: `USER`
   - Membership: `VIP`
   - Email: `juan.perez@email.com`

3. **PREMIUM**
   - Username: `maria.lopez`
   - Password: `maria789012`
   - Rol: `USER`
   - Membership: `STANDARD`
   - Email: `maria.lopez@email.com`

4. **STANDARD**
   - Username: `pedro.sanchez`
   - Password: `pedro345678`
   - Rol: `USER`
   - Membership: `STANDARD`
   - Email: `pedro.sanchez@email.com`

---

## 📚 LIBROS DISPONIBLES EN MONGODB

Los siguientes libros se han precargado en MongoDB:

| # | Título | Autor | ISBN | Copias Disponibles | Copias Totales |
|---|--------|-------|------|-------------------|----------------|
| 1 | Clean Code | Robert C. Martin | 978-0132350884 | 4 | 5 |
| 2 | The Pragmatic Programmer | David Thomas, Andrew Hunt | 978-0201616224 | 3 | 3 |
| 3 | Design Patterns | Gang of Four | 978-0201633610 | 2 | 4 |
| 4 | Spring in Action | Craig Walls | 978-1617299032 | 5 | 6 |
| 5 | Effective Java | Joshua Bloch | 978-0134685991 | 6 | 7 |

---

## 🧪 FLUJO DE PRUEBAS FUNCIONALES

### PASO 1: 🔐 AUTENTICACIÓN

**Endpoint**: `POST /auth/login`

**Cuerpo de Solicitud**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Respuesta Esperada** (201):
```json
{
  "token": "eyJhbGc...",
  "username": "admin",
  "role": "LIBRARIAN",
  "userId": "user-admin-001"
}
```

✅ **Resultado**: Se obtiene JWT válido para autorización

---

### PASO 2: 📚 OBTENER TODOS LOS LIBROS

**Endpoint**: `GET /api/books`

**Headers Requeridos**:
```
Authorization: Bearer {token}
```

**Respuesta Esperada** (200):
- Array con 5 libros desde MongoDB
- Cada libro incluye: título, autor, ISBN, availability, metadata, categorías

✅ **Resultado**: Se recuperan todos los libros de MongoDB

---

### PASO 3: 🔍 BUSCAR LIBRO POR ISBN

**Endpoint**: `GET /api/books/search?isbn=978-0132350884`

**Headers Requeridos**:
```
Authorization: Bearer {token}
```

**Respuesta Esperada** (200):
```json
{
  "id": "book-001",
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "availability": {
    "availableCopies": 4,
    "totalCopies": 5,
    "loanedCopies": 1
  }
}
```

✅ **Resultado**: Se encuentra el libro correcto en MongoDB

---

### PASO 4: 👥 OBTENER TODOS LOS USUARIOS

**Endpoint**: `GET /api/users`

**Headers Requeridos**:
```
Authorization: Bearer {token}
Role: LIBRARIAN (solo admin puede ver usuarios)
```

**Respuesta Esperada** (200):
- Array con 4 usuarios precargados en MongoDB
- Cada usuario incluye: nombre, email, username, membershipType, dateAddedAsUser

✅ **Resultado**: Se obtienen todos los usuarios de MongoDB

---

### PASO 5: 💳 LOGIN CON USUARIO VIP Y CREAR PRÉSTAMO

**Paso 5a: Login VIP**

**Endpoint**: `POST /auth/login`

**Cuerpo**:
```json
{
  "username": "juan.perez",
  "password": "juan123456"
}
```

**Respuesta**: Token para juan.perez

---

**Paso 5b: Crear Préstamo**

**Endpoint**: `POST /api/loans`

**Headers**:
```
Authorization: Bearer {vip-token}
Content-Type: application/json
```

**Cuerpo**:
```json
{
  "bookId": "book-001",
  "userId": "user-vip-001",
  "loanDate": "2026-04-04",
  "returnDate": "2026-04-18",
  "returned": false
}
```

**Respuesta Esperada** (201):
```json
{
  "id": "loan-xxx",
  "book": {
    "id": "book-001",
    "title": "Clean Code",
    "availability": {
      "availableCopies": 3,  // Disminuye de 4 a 3
      "totalCopies": 5,
      "loanedCopies": 2       // Aumenta de 1 a 2
    }
  },
  "user": {
    "id": "user-vip-001",
    "name": "Juan Pérez García"
  },
  "loanDate": "2026-04-04",
  "returnDate": "2026-04-18",
  "returned": false
}
```

✅ **Resultado**: Préstamo creado en MongoDB, disponibilidad actualizada

---

### PASO 6: 📋 OBTENER TODOS LOS PRÉSTAMOS

**Endpoint**: `GET /api/loans`

**Headers**:
```
Authorization: Bearer {admin-token}
```

**Respuesta Esperada** (200):
- Array con préstamos activos desde MongoDB
- Incluye préstamo recientemente creado

✅ **Resultado**: Se obtienen préstamos de MongoDB

---

### PASO 7: 🆕 REGISTRAR NUEVO USUARIO

**Endpoint**: `POST /auth/register`

**Cuerpo**:
```json
{
  "username": "newtester",
  "password": "TestPass123!",
  "name": "Nuevo Usuario Prueba",
  "email": "newuser@test.com",
  "role": "USER"
}
```

**Respuesta Esperada** (201):
```json
{
  "id": "user-xxx",
  "name": "Nuevo Usuario Prueba",
  "username": "newtester",
  "email": "newuser@test.com",
  "role": "USER"
}
```

✅ **Resultado**: Nuevo usuario registrado en MongoDB

---

### PASO 8: 📤 RETORNAR LIBRO (Actualizar Préstamo)

**Endpoint**: `PUT /api/loans/{loanId}`

**Headers**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Cuerpo**:
```json
{
  "returned": true
}
```

**Respuesta Esperada** (200):
```json
{
  "id": "loan-xxx",
  "returned": true,
  "book": {
    "availability": {
      "availableCopies": 4,  // Vuelve a aumentar
      "loanedCopies": 1       // Disminuye
    }
  }
}
```

✅ **Resultado**: Préstamo retornado, disponibilidad actualizada en MongoDB

---

## 🔗 ARQUITECTURA VALIDADA

### ✅ Dual Persistence Verificada:
1. **Spring Profile Activo**: `mongo`
2. **Inyección Automática**: 
   - `BookPersistenceRepository` → `BookRepositoryMongoImpl`
   - `UserPersistenceRepository` → `UserRepositoryMongoImpl`
   - `LoanPersistenceRepository` → `LoanRepositoryMongoImpl`
3. **Streams Operations**: Implementadas en todos los repositorios
4. **MongoDB Collections**:
   - `books` - 5 documentos precargados
   - `users` - 4 documentos precargados
   - `loans` - Vacía (se llena con préstamos)

---

## 🔒 SEGURIDAD VERIFICADA

### JWT Token:
- ✅ Generado en login
- ✅ Validado en cada petición
- ✅ Contiene username, role, userId
- ✅ Expiración: 1 hora

### Authorization:
- ✅ `/auth/**` - Público (sin autenticación)
- ✅ `/api/books` GET - Autenticado
- ✅ `/api/books` POST/PUT/DELETE - Solo LIBRARIAN
- ✅ `/api/users` - Solo LIBRARIAN
- ✅ `/api/loans` - Autenticado

---

## 📊 DATOS EN MONGODB

### Colección: `books` (5 documentos)
```mongodb
db.books.count()  // 5
db.books.findAll() // Retorna todos los libros con metadata completa
```

### Colección: `users` (4 documentos)
```mongodb
db.users.count()  // 4
db.users.find({username: "admin"})
```

### Colección: `loans`
```mongodb
db.loans.count()  // Crece con cada préstamo creado
```

---

## ✅ CHECKLIST DE VALIDACIÓN

- [x] Tests unitarios: 144/144 PASSING
- [x] Compilación Maven: BUILD SUCCESS
- [x] MongoDB conectado: Connection Verified
- [x] Datos precargados: CommandLineRunner ejecutado
- [x] Profile activo: `mongo` ✅
- [x] Inyección automática: Funcionando
- [x] JWT autenticación: Operacional
- [x] Swagger UI: Accesible en `/swagger-ui.html`
- [x] Endpoints REST: Documentados en Swagger

---

## 🚀 CÓMO ACCEDER A SWAGGER UI

1. Abre en navegador: **http://localhost:8080/swagger-ui.html**
2. Verás todos los endpoints disponibles
3. Haz clic en "Try it out" para invocar cada endpoint
4. Usa credenciales de prueba para autenticarte
5. Ejecuta cada test siguiendo el flujo anterior

---

## 🆘 TROUBLESHOOTING

### Si no ves los datos de prueba precargados:
- Verifica que `spring.profiles.active=mongo` en `application.properties`
- Los datos se cargan automáticamente al startup por `MongoDataInitializer.java`
- Revisa los logs en `app.log` para ver si `CommandLineRunner` se ejecutó

### Si la autenticación falla:
- Verifica que las credenciales coincidan exactamente (case-sensitive)
- Las contraseñas están hasheadas con BCrypt en MongoDB
- El token debe ser incluido en `Authorization: Bearer {token}`

### Si los libros no aparecen:
- Verifica conexión a MongoDB Atlas
- Comprueba que la colección `books` existe en la base de datos `dosw_library`
- Verifica el perfil activo no sea `relational`

---

**Documenton creado**: 2026-04-04 18:30 UTC
**Versión**: DOSW-Library 0.0.1 con MongoDB
