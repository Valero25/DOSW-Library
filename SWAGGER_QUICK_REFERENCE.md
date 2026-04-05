# 🚀 REFERENCIA RÁPIDA - PRUEBAS EN SWAGGER

## 🔗 ABRIR SWAGGER UI
```
http://localhost:8080/swagger-ui.html
```

---

## 🎬 PRUEBAS RÁPIDAS (5 minutos)

### TEST 1️⃣: LOGIN
```
POST /auth/login
Body: {"username":"admin","password":"admin123"}
✅ Copiar el "token" devuelto
```

### TEST 2️⃣: AUTORIZAR EN SWAGGER
```
1. Haz clic en botón "Authorize" (arriba a la derecha)
2. Pega: Bearer {tu_token_aquí}
3. Clic en "Authorize" y "Close"
```

### TEST 3️⃣: GET LIBROS
```
GET /api/books
✅ Deberías ver 5 libros desde MongoDB
```

### TEST 4️⃣: GET USUARIOS
```
GET /api/users (requiere LIBRARIAN)
✅ Deberías ver 4 usuarios
```

### TEST 5️⃣: BUSCAR LIBRO
```
GET /api/books/search?isbn=978-0132350884
✅ Devuelve "Clean Code"
```

### TEST 6️⃣: CREAR PRÉSTAMO
```
Primero login con: juan.perez / juan123456
Luego POST /api/loans
Body: {
  "bookId":"book-001",
  "userId":"user-vip-001", 
  "loanDate":"2026-04-04",
  "returnDate":"2026-04-18",
  "returned":false
}
✅ Devuelve status 201 + préstamo creado
```

### TEST 7️⃣: GET PRÉSTAMOS
```
GET /api/loans
✅ Ver préstamos activos
```

### TEST 8️⃣: RETORNAR LIBRO
```
PUT /api/loans/{loanId}
Body: {"returned":true}
✅ Libro marcado como retornado
```

### TEST 9️⃣: REGISTRAR USUARIO
```
POST /auth/register
Body: {
  "username":"newuser",
  "password":"Pass123!",
  "name":"New User",
  "email":"new@test.com",
  "role":"USER"
}
✅ Usuario creado
```

---

## 👤 CREDENCIALES DE PRUEBA

| Username | Password | Rol |
|----------|----------|-----|
| admin | admin123 | LIBRARIAN |
| juan.perez | juan123456 | USER (VIP) |
| maria.lopez | maria789012 | USER |
| pedro.sanchez | pedro345678 | USER |

---

## 📚 LIBROS DISPONIBLES

| ISBN | Título |
|------|--------|
| 978-0132350884 | Clean Code |
| 978-0201616224 | The Pragmatic Programmer |
| 978-0201633610 | Design Patterns |
| 978-1617299032 | Spring in Action |
| 978-0134685991 | Effective Java |

---

## ⚠️ PUNTOS IMPORTANTES

✅ **Todos los endpoints requieren JWT** (excepto /auth/login y /auth/register)
✅ **Los datos están en MongoDB** (no en PostgreSQL)
✅ **LIBRARIAN puede hacer CRUD** en libros y usuarios
✅ **USER solo puede hacer loans**
✅ **Los cambios persisten** en MongoDB

---

## 🔴 ERRORES COMUNES

| Error | Causa | Solución |
|-------|-------|----------|
| 401 Unauthorized | No hay token | Haz login y autoriza en Swagger |
| 403 Forbidden | Usuario no tiene rol | Usa usuario admin para operaciones LIBRARIAN |
| 404 Not Found | Recurso no existe | Verifica el ID del recurso |
| 400 Bad Request | Datos inválidos | Revisa el formato del JSON |

---

✅ **¡Listo para probar!**
