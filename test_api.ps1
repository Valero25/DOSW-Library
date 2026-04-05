#!/usr/bin/env pwsh
# Script de Pruebas Funcionales - DOSW-Library
# Simula un flujo real de biblioteca con MongoDB

$BaseUrl = "http://localhost:8080"
$token = ""

Write-Host "╔══════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║   PRUEBAS FUNCIONALES - DOSW-LIBRARY (MongoDB)          ║" -ForegroundColor Cyan
Write-Host "╚══════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# ============================================================
# TEST 1: LOGIN CON USUARIO ADMIN
# ============================================================
Write-Host "TEST 1: 🔐 LOGIN CON USUARIO ADMIN" -ForegroundColor Green
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
try {
    $loginBody = @{username="admin"; password="admin123"} | ConvertTo-Json
    $resp = Invoke-WebRequest -Uri "$BaseUrl/auth/login" `
        -Method POST -UseBasicParsing -Headers @{"Content-Type"="application/json"} `
        -Body $loginBody 2>&1
    
    if ($resp.StatusCode -eq 200) {
        $data = $resp.Content | ConvertFrom-Json
        $token = $data.token
        Write-Host "✅ Login exitoso" -ForegroundColor Green
        Write-Host "   Usuario: $($data.username)" -ForegroundColor Yellow
        Write-Host "   Rol: $($data.role)" -ForegroundColor Yellow
        Write-Host "   UserId: $($data.userId)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# ============================================================
# TEST 2: OBTENER TODOS LOS LIBROS
# ============================================================
Write-Host "TEST 2: 📚 OBTENER TODOS LOS LIBROS" -ForegroundColor Green
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
try {
    $headers = @{"Authorization" = "Bearer $token"; "Content-Type" = "application/json"}
    $resp = Invoke-WebRequest -Uri "$BaseUrl/api/books" `
        -Method GET -UseBasicParsing -Headers $headers 2>&1
    
    if ($resp.StatusCode -eq 200) {
        $books = $resp.Content | ConvertFrom-Json
        Write-Host "✅ Libros obtenidos: $($books.Count)" -ForegroundColor Green
        $books | ForEach-Object {
            Write-Host "   📖 $($_.title) - Autor: $($_.author)" -ForegroundColor Yellow
            Write-Host "      ISBN: $($_.isbn) | Disponibles: $($_.availability.availableCopies)/$($_.availability.totalCopies)" -ForegroundColor Gray
        }
    }
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# ============================================================
# TEST 3: OBTENER TODOS LOS USUARIOS
# ============================================================
Write-Host "TEST 3: 👥 OBTENER TODOS LOS USUARIOS" -ForegroundColor Green
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
try {
    $headers = @{"Authorization" = "Bearer $token"; "Content-Type" = "application/json"}
    $resp = Invoke-WebRequest -Uri "$BaseUrl/api/users" `
        -Method GET -UseBasicParsing -Headers $headers 2>&1
    
    if ($resp.StatusCode -eq 200) {
        $users = $resp.Content | ConvertFrom-Json
        Write-Host "✅ Usuarios obtenidos: $($users.Count)" -ForegroundColor Green
        $users | ForEach-Object {
            Write-Host "   👤 $($_.name) (@$($_.username))" -ForegroundColor Yellow
            Write-Host "      Email: $($_.email) | Membership: $($_.membershipType)" -ForegroundColor Gray
        }
    }
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# ============================================================
# TEST 4: BUSCAR LIBRO POR ISBN
# ============================================================
Write-Host "TEST 4: 🔍 BUSCAR LIBRO POR ISBN" -ForegroundColor Green
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
try {
    $headers = @{"Authorization" = "Bearer $token"; "Content-Type" = "application/json"}
    $isbn = "978-0132350884"
    $resp = Invoke-WebRequest -Uri "$BaseUrl/api/books/search?isbn=$isbn" `
        -Method GET -UseBasicParsing -Headers $headers 2>&1
    
    if ($resp.StatusCode -eq 200) {
        $book = $resp.Content | ConvertFrom-Json
        Write-Host "✅ Libro encontrado" -ForegroundColor Green
        Write-Host "   Título: $($book.title)" -ForegroundColor Yellow
        Write-Host "   Autor: $($book.author)" -ForegroundColor Yellow
        Write-Host "   ISBN: $($book.isbn)" -ForegroundColor Yellow
        Write-Host "   Disponibles: $($book.availability.availableCopies)/$($book.availability.totalCopies)" -ForegroundColor Gray
    }
} catch {
    Write-Host "⚠️  No encontrado o error: $($_.Exception.Message)" -ForegroundColor Yellow
}
Write-Host ""

# ============================================================
# TEST 5: LOGIN CON USUARIO VIP Y CREAR PRÉSTAMO
# ============================================================
Write-Host "TEST 5: 💳 LOGIN USUARIO VIP Y CREAR PRÉSTAMO" -ForegroundColor Green
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
try {
    # Login con usuario VIP
    $vipLoginBody = @{username="juan.perez"; password="juan123456"} | ConvertTo-Json
    $vipResp = Invoke-WebRequest -Uri "$BaseUrl/auth/login" `
        -Method POST -UseBasicParsing -Headers @{"Content-Type"="application/json"} `
        -Body $vipLoginBody 2>&1
    
    if ($vipResp.StatusCode -eq 200) {
        $vipData = $vipResp.Content | ConvertFrom-Json
        $vipToken = $vipData.token
        $vipUserId = $vipData.userId
        Write-Host "✅ Login usuario VIP exitoso: $($vipData.username)" -ForegroundColor Green
        
        # Crear préstamo
        $loanBody = @{
            bookId = "book-001"
            userId = $vipUserId
            loanDate = (Get-Date).ToString("yyyy-MM-dd")
            returnDate = (Get-Date).AddDays(14).ToString("yyyy-MM-dd")
            returned = $false
        } | ConvertTo-Json
        
        $headers = @{"Authorization" = "Bearer $vipToken"; "Content-Type" = "application/json"}
        $loanResp = Invoke-WebRequest -Uri "$BaseUrl/api/loans" `
            -Method POST -UseBasicParsing -Headers $headers `
            -Body $loanBody 2>&1
        
        if ($loanResp.StatusCode -eq 201) {
            $loan = $loanResp.Content | ConvertFrom-Json
            Write-Host "✅ Préstamo creado exitosamente" -ForegroundColor Green
            Write-Host "   ID Préstamo: $($loan.id)" -ForegroundColor Yellow
            Write-Host "   Libro: $($loan.book.title)" -ForegroundColor Yellow
            Write-Host "   Usuario: $($loan.user.name)" -ForegroundColor Yellow
            Write-Host "   Fecha Retorno: $($loan.returnDate)" -ForegroundColor Gray
        }
    }
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# ============================================================
# TEST 6: OBTENER TODOS LOS PRÉSTAMOS
# ============================================================
Write-Host "TEST 6: 📋 OBTENER TODOS LOS PRÉSTAMOS" -ForegroundColor Green
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
try {
    $headers = @{"Authorization" = "Bearer $token"; "Content-Type" = "application/json"}
    $resp = Invoke-WebRequest -Uri "$BaseUrl/api/loans" `
        -Method GET -UseBasicParsing -Headers $headers 2>&1
    
    if ($resp.StatusCode -eq 200) {
        $loans = $resp.Content | ConvertFrom-Json
        Write-Host "✅ Préstamos obtenidos: $($loans.Count)" -ForegroundColor Green
        $loans | ForEach-Object {
            $status = if ($_.returned) { "✅ RETORNADO" } else { "⏳ ACTIVO" }
            Write-Host "   $status | Usuario: $($_.user.name) | Libro: $($_.book.title)" -ForegroundColor Yellow
            Write-Host "      Fecha Retorno: $($_.returnDate)" -ForegroundColor Gray
        }
    }
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# ============================================================
# TEST 7: REGISTRAR NUEVO USUARIO
# ============================================================
Write-Host "TEST 7: 🆕 REGISTRAR NUEVO USUARIO" -ForegroundColor Green
Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
try {
    $timestamp = Get-Date -Format "yyyyMMddHHmmss"
    $newUserBody = @{
        username = "newuser_$timestamp"
        password = "SecurePass123!"
        name = "Nuevo Usuario Prueba"
        email = "newuser_$timestamp@test.com"
        role = "USER"
    } | ConvertTo-Json
    
    $resp = Invoke-WebRequest -Uri "$BaseUrl/auth/register" `
        -Method POST -UseBasicParsing -Headers @{"Content-Type"="application/json"} `
        -Body $newUserBody 2>&1
    
    if ($resp.StatusCode -eq 201) {
        $newUser = $resp.Content | ConvertFrom-Json
        Write-Host "✅ Usuario registrado exitosamente" -ForegroundColor Green
        Write-Host "   ID: $($newUser.id)" -ForegroundColor Yellow
        Write-Host "   Username: $($newUser.username)" -ForegroundColor Yellow
        Write-Host "   Email: $($newUser.email)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# ============================================================
# RESUMEN
# ============================================================
Write-Host "╔══════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║            ✅ PRUEBAS COMPLETADAS                        ║" -ForegroundColor Cyan
Write-Host "║   Base de Datos: MongoDB (Cluster0 Atlas)               ║" -ForegroundColor Cyan
Write-Host "║   API: Spring Boot 3.2.3 + Java 21                      ║" -ForegroundColor Cyan
Write-Host "║   URL: http://localhost:8080/swagger-ui.html            ║" -ForegroundColor Cyan
Write-Host "╚══════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""
