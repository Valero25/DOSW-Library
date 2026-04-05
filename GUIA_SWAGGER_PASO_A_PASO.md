# 🎬 GUÍA VISUAL: EJECUTAR PRUEBAS EN SWAGGER UI

## PASO 0️⃣: ABRIR SWAGGER UI

1. **Abre tu navegador**
2. **Ve a**: http://localhost:8080/swagger-ui.html
3. Deberías ver una interfaz con todos los endpoints organizados por categorías

---

## 📝 PRUEBA 1: LOGIN (OBTENER TOKEN JWT)

### 🎯 Objetivo: Autenticarse como administrador

1. **En Swagger, busca la sección "Autenticación"** (o scroll down)
2. **Expande "POST /auth/login"** (haz clic en la fila)
3. **Haz clic en "Try it out"** (botón azul en la derecha)
4. **En el campo de texto "Request body", borra el contenido y pega:**

```json
{
  "username": "admin",
  "password": "admin123"
}
```

5. **Haz clic en "Execute"** (botón azul grande)
6. **Observa la respuesta bajo "Responses":**

```json
{
  "token": "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "role": "LIBRARIAN",
  "userId": "user-admin-001"
}
```

7. **📌 COPIA EL VALOR DEL TOKEN** (la cadena larga que comienza con "eyJ")

✅ **RESULTADO**: Has obtenido un token JWT válido

---

## 🔑 PASO IMPORTANTE: AUTORIZAR EN SWAGGER

### 🎯 Objetivo: Añadir el token a todos los próximos requests

1. **En la parte SUPERIOR de Swagger, busca el botón "Authorize"** 
   *(Es un botón verde generalmente en la esquina superior derecha)*

2. **Haz clic en "Authorize"**

3. **Se abrirá un modal. En el campo de texto, pega:**

```
Bearer eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9...
```

(Reemplaza con tu token actual. El `Bearer ` es importante - no lo olvides)

4. **Haz clic en "Authorize"** 

5. **Haz clic en "Close"**

✅ **Ahora todos tus requests incluirán el token automáticamente**

---

## 📚 PRUEBA 2: OBTENER TODOS LOS LIBROS

### 🎯 Objetivo: Recuperar los 5 libros precargados en MongoDB

1. **Busca la sección "Libros"**
2. **Expande "GET /api/books"**
3. **Haz clic en "Try it out"**
4. **Haz clic en "Execute"**

### ✅ Respuesta Esperada (Status 200):

```json
[
  {
    "id": "book-001",
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "available": true,
    "categories": ["Programming", "Software Engineering"],
    "publicationType": "LIBRO",
    "metadata": {
      "pages": 464,
      "language": "English",
      "publisher": "Prentice Hall"
    },
    "availability": {
      "status": "Disponible",
      "totalCopies": 5,
      "availableCopies": 4,
      "loanedCopies": 1
    }
  },
  // ... más libros
]
```

**¿Ves los libros?** ✅ Esto significa que MongoDB está funcionando correctamente

---

## 🔍 PRUEBA 3: BUSCAR LIBRO POR ISBN

### 🎯 Objetivo: Validar búsqueda específica

1. **Busca "GET /api/books/search"**
2. **Haz clic en "Try it out"**
3. **En el parámetro "isbn", ingresa:**

```
978-0132350884
```

4. **Haz clic en "Execute"**

### ✅ Respuesta Esperada (Status 200):

Se retorna el libro "Clean Code" con todos sus detalles

---

## 👥 PRUEBA 4: OBTENER TODOS LOS USUARIOS

### 🎯 Objetivo: Ver los 4 usuarios precargados (requiere rol LIBRARIAN)

1. **Busca "GET /api/users"**
2. **Haz clic en "Try it out"**
3. **Haz clic en "Execute"**

### ✅ Respuesta Esperada (Status 200):

```json
[
  {
    "id": "user-admin-001",
    "name": "Carlos Administrador",
    "email": "admin@dosw-library.com",
    "username": "admin",
    "role": "LIBRARIAN",
    "membershipType": "PLATINUM",
    "dateAddedAsUser": "2026-04-04"
  },
  {
    "id": "user-vip-001",
    "name": "Juan Pérez García",
    "email": "juan.perez@email.com",
    "username": "juan.perez",
    "role": "USER",
    "membershipType": "VIP",
    "dateAddedAsUser": "2026-03-05"
  },
  // ... más usuarios
]
```

---

## 💳 PRUEBA 5: CREAR UN PRÉSTAMO (IMPORTANTE CREAR PRIMERO UN USUARIO VIP)

### 🎯 Objetivo: Simular que un usuario pide prestado un libro

#### PRIMER PASO: Login con usuario VIP

1. **Haz logout del token anterior:**
   - Haz clic en "Authorize" (arriba)
   - Haz clic en "Logout"

2. **Haz login con usuario VIP:**
   - Busca "POST /auth/login"
   - Haz clic en "Try it out"
   - Ingresa:

```json
{
  "username": "juan.perez",
  "password": "juan123456"
}
```

3. **Copia el nuevo token JWT y autoriza Swagger nuevamente**

#### SEGUNDO PASO: Crear Préstamo

1. **Busca "POST /api/loans"**
2. **Haz clic en "Try it out"**
3. **En el body, ingresa:**

```json
{
  "bookId": "book-001",
  "userId": "user-vip-001",
  "loanDate": "2026-04-04",
  "returnDate": "2026-04-18",
  "returned": false
}
```

4. **Haz clic en "Execute"**

### ✅ Respuesta Esperada (Status 201):

```json
{
  "id": "loan-xxx",
  "book": {
    "id": "book-001",
    "title": "Clean Code",
    "availability": {
      "availableCopies": 3,  // ← Disminuye de 4 a 3
      "totalCopies": 5,
      "loanedCopies": 2      // ← Aumenta de 1 a 2
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

**¡El préstamo se creó exitosamente!** ✅

---

## 📋 PRUEBA 6: OBTENER TODOS LOS PRÉSTAMOS

### 🎯 Objetivo: Ver los préstamos activos

1. **Busca "GET /api/loans"**
2. **Haz clic en "Try it out"**
3. **Haz clic en "Execute"**

### ✅ Respuesta Esperada (Status 200):

Verás un array con el préstamo que acabas de crear

---

## 🆕 PRUEBA 7: REGISTRAR UN NUEVO USUARIO

### 🎯 Objetivo: Crear un usuario prueba nuevo

1. **Busca "POST /auth/register"**
2. **Haz clic en "Try it out"**
3. **En el body, ingresa:**

```json
{
  "username": "testuser123",
  "password": "TestPass123!",
  "name": "Usuario de Prueba",
  "email": "test@ejemplo.com",
  "role": "USER"
}
```

4. **Haz clic en "Execute"**

### ✅ Respuesta Esperada (Status 201):

```json
{
  "id": "user-xxx",
  "name": "Usuario de Prueba",
  "username": "testuser123",
  "email": "test@ejemplo.com",
  "role": "USER"
}
```

**¡Nuevo usuario creado en MongoDB!** ✅

---

## 📤 PRUEBA 8: RETORNAR UN LIBRO

### 🎯 Objetivo: Marcar un préstamo como retornado

1. **Busca "PUT /api/loans/{id}"**
2. **Haz clic en "Try it out"**
3. **En el parámetro "id", ingresa:** 

El ID del préstamo que creaste (ej: `loan-xxx`)

4. **En el body, ingresa:**

```json
{
  "returned": true
}
```

5. **Haz clic en "Execute"**

### ✅ Respuesta Esperada (Status 200):

```json
{
  "id": "loan-xxx",
  "returned": true,
  "book": {
    "availability": {
      "availableCopies": 4,  // ← Vuelve a aumentar
      "loanedCopies": 1       // ← Disminuye
    }
  }
}
```

**¡El libro fue retornado!** ✅

---

## 🧪 PRUEBA 9: INTENTAR SIN AUTENTICACIÓN (Para ver error 401)

### 🎯 Objetivo: Validar que la seguridad funciona

1. **Haz clic en "Authorize"**
2. **Haz clic en "Logout"**
3. **Busca "GET /api/books"**
4. **Haz clic en "Try it out"**
5. **Haz clic en "Execute"**

### ❌ Respuesta Esperada (Status 401):

```json
{
  "error": "Unauthorized",
  "message": "No token provided"
}
```

**¡La seguridad está funcionando correctamente!** ✅

---

## 🔐 PRUEBA 10: INTENTAR OPERACIÓN SOLO LIBRARIAN (Como USER normal)

### 🎯 Objetivo: Validar control de roles

1. **Haz login con usuario NON-LIBRARIAN:**
   - Username: `juan.perez`
   - Password: `juan123456`
   - (Rol: USER, no LIBRARIAN)

2. **Busca "POST /api/books"** (crear libro - solo para LIBRARIAN)
3. **Haz clic en "Try it out"**
4. **Ingresa datos de un libro nuevo**
5. **Haz clic en "Execute"**

### ❌ Respuesta Esperada (Status 403):

```json
{
  "error": "Forbidden",
  "message": "Acceso denegado para este rol"
}
```

**¡El control de roles está funcionando!** ✅

---

## 📊 RESUMEN DE PRUEBAS

Tras completar todas estas pruebas, habrás validado:

| ✅ Aspecto | Pruebas |
|-----------|---------|
| **Autenticación JWT** | Login, tokens válidos |
| **MongoDB Integration** | Lectura de 5 libros, 4 usuarios |
| **CRUD Operations** | Create, Read, Update en libros, usuarios, préstamos |
| **Business Logic** | Actualización automática de disponibilidad |
| **Security** | JWT requerido, RBAC (roles) |
| **Data Persistence** | Datos persisten en MongoDB |
| **API Documentation** | Swagger UI funcional |

---

## 🐛 TROUBLESHOOTING

### ❌ "Cannot get /api/books" (404)
- Verifica que la URL sea http://localhost:8080/swagger-ui.html
- Recarga la página

### ❌ "No token provided" (401)
- Verifica que hayas hecho clic en "Authorize" y pegado el token correctamente
- Formato correcto: `Bearer eyJ...`

### ❌ "User not found" (400)
- Verifica que usaste exactamente las credenciales:
  - admin / admin123
  - juan.perez / juan123456

### ❌ "Forbidden" (403)
- Estás intentando operación de LIBRARIAN con rol USER
- Usa usuario `admin` para crear/modificar libros

---

## 🎯 PRÓXIMO PASO

Una vez completes todas estas pruebas:

1. Los datos estarán en MongoDB 
2. Podrás ver que todo funciona desde una interfaz visual
3. Tendrás validado que:
   - ✅ API REST funciona
   - ✅ MongoDB persiste datos
   - ✅ JWT autentica usuarios
   - ✅ RBAC controla acceso
   - ✅ Lógica de negocio funciona

**¡Entonces sabrás que la implementación es exitosa!**

---

Documento: Guía Visual Swagger UI
Fecha: 2026-04-04
Status: LISTO PARA USAR ✅
