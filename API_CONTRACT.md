# Contrato Oficial - API REST Condominio

Este documento define el contrato estricto de comunicación entre los clientes (React, Flutter, Escritorio) y el backend Spring Boot. Ningún endpoint debe desviarse de esta estructura.

## Estructura Base de Respuesta (ApiResponse)
Absolutamente todas las respuestas de la API están envueltas en el siguiente formato JSON unificado, independiente de si la operación fue exitosa o fallida:

```json
{
  "status": 200,          // Código HTTP repetido por seguridad en clientes
  "message": "Mensaje",   // Mensaje legible para el usuario final
  "data": { ... },        // Payload de respuesta (nulo en errores o en DELETE)
  "errors": [ ... ]       // Arreglo de errores de validación (solo si status = 400)
}
```

---

## 1. Módulo de Autenticación (`AuthController`)

### Iniciar Sesión
- **Método HTTP:** `POST`
- **URL:** `/api/v1/auth/login`
- **Headers requeridos:** `Content-Type: application/json`
- **Si requiere JWT:** No
- **DTO Utilizado:** Request: `LoginRequest` | Response: `AuthResponse`
- **Códigos HTTP posibles:** `200 OK`, `400 Bad Request`, `401 Unauthorized`

#### Request JSON
```json
{
  "usuario": "admin",
  "password": "password123"
}
```

#### Response JSON
```json
{
  "status": 200,
  "message": "Login exitoso",
  "data": {
    "accessToken": "eyJhbG...",
    "refreshToken": "d7a8b9...",
    "expiresIn": 900
  }
}
```

### Renovar Token
- **Método HTTP:** `POST`
- **URL:** `/api/v1/auth/refresh`
- **Headers requeridos:** `Content-Type: application/json`
- **Si requiere JWT:** No
- **DTO Utilizado:** Request: `RefreshTokenRequest` | Response: `AuthResponse`
- **Códigos HTTP posibles:** `200 OK`, `400 Bad Request`, `401 Unauthorized`

#### Request JSON
```json
{
  "refreshToken": "d7a8b9..."
}
```

#### Response JSON
```json
{
  "status": 200,
  "message": "Token renovado exitosamente",
  "data": {
    "accessToken": "eyJhbG...",
    "refreshToken": "d7a8b9...",
    "expiresIn": 900
  }
}
```

---

## 2. Módulo de Tickets / Incidencias (`TicketController`)

### Crear Ticket
- **Método HTTP:** `POST`
- **URL:** `/api/v1/tickets`
- **Headers requeridos:** `Content-Type: application/json`, `Authorization: Bearer <token>`
- **Si requiere JWT:** Sí
- **DTO Utilizado:** Request: `TicketRequest` | Response: `TicketResponse`
- **Códigos HTTP posibles:** `201 Created`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`

#### Request JSON
```json
{
  "titulo": "Fuga de agua en pasillo",
  "descripcion": "El pasillo del piso 3 presenta acumulación de agua.",
  "prioridad": "ALTA",
  "categoriaId": 2
}
```

#### Response JSON
```json
{
  "status": 201,
  "message": "Ticket creado exitosamente",
  "data": {
    "id": 15,
    "titulo": "Fuga de agua en pasillo",
    "estado": "ABIERTO",
    "fechaCreacion": "2026-07-07T10:00:00Z"
  }
}
```

### Consultar Ticket por ID
- **Método HTTP:** `GET`
- **URL:** `/api/v1/tickets/{id}`
- **Headers requeridos:** `Authorization: Bearer <token>`
- **Si requiere JWT:** Sí
- **DTO Utilizado:** Request: `N/A` | Response: `TicketResponse`
- **Códigos HTTP posibles:** `200 OK`, `401 Unauthorized`, `404 Not Found`

#### Request JSON
*Vacío*

#### Response JSON
```json
{
  "status": 200,
  "message": "Ticket obtenido",
  "data": {
    "id": 15,
    "titulo": "Fuga de agua en pasillo",
    "estado": "ABIERTO",
    "fechaCreacion": "2026-07-07T10:00:00Z"
  }
}
```

---

## 3. Módulo de Visitantes (`VisitanteController`)

### Registrar Nuevo Visitante
- **Método HTTP:** `POST`
- **URL:** `/api/v1/visitantes`
- **Headers requeridos:** `Content-Type: application/json`, `Authorization: Bearer <token>`
- **Si requiere JWT:** Sí
- **DTO Utilizado:** Request: `VisitanteRequest` | Response: `VisitanteResponse`
- **Códigos HTTP posibles:** `201 Created`, `400 Bad Request`, `401 Unauthorized`, `403 Forbidden`

#### Request JSON
```json
{
  "nombreCompleto": "Juan Pérez",
  "documentoIdentidad": "1234567890",
  "motivo": "Visita familiar",
  "unidadId": 45
}
```

#### Response JSON
```json
{
  "status": 201,
  "message": "Visitante registrado exitosamente",
  "data": {
    "id": 102,
    "nombreCompleto": "Juan Pérez",
    "fechaIngreso": "2026-07-07T10:30:00Z"
  }
}
```

---

## Estándar de Errores (GlobalExceptionHandler)

El backend **garantiza** que **ningún error de validación del cliente retornará HTTP 500**. Todos los errores del usuario serán encapsulados con un código HTTP apropiado:

### Ejemplo de Error de Validación (HTTP 400 Bad Request)
Ocurre cuando el cliente envía un JSON con campos vacíos, inválidos o el JSON está mal formado (`HttpMessageNotReadableException`).

```json
{
  "status": 400,
  "message": "Error de validación. Revisa los campos indicados.",
  "data": null,
  "errors": [
    {
      "campo": "titulo",
      "mensaje": "El título no puede estar en blanco"
    },
    {
      "campo": "categoriaId",
      "mensaje": "Debe proporcionar una categoría válida"
    }
  ]
}
```

### Ejemplo de Recurso No Encontrado (HTTP 404 Not Found)
Ocurre cuando se busca un ID que no existe en BD (`ResourceNotFoundException`).

```json
{
  "status": 404,
  "message": "El ticket con ID 999 no existe.",
  "data": null,
  "errors": []
}
```

### Ejemplo de Acceso Denegado (HTTP 403 Forbidden)
Ocurre cuando el usuario tiene un JWT válido pero carece del Rol necesario para la acción.

```json
{
  "status": 403,
  "message": "No tienes permisos para realizar esta acción.",
  "data": null,
  "errors": []
}
```
