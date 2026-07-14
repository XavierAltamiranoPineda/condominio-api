# Plan de Pruebas API REST - CondoAdmin

## 1. Introducción

El presente documento define el plan de pruebas aplicado a la API REST del sistema CondoAdmin, una plataforma de gestión de condominios basada en una arquitectura cliente-servidor.

La API REST desarrollada en Spring Boot funciona como núcleo central del sistema, permitiendo la comunicación entre los diferentes clientes:

- Aplicación Web.
- Aplicación Desktop desarrollada en Python/PyQt6.
- Aplicación móvil Flutter.

El objetivo principal de las pruebas es verificar que los servicios REST funcionen correctamente, validando autenticación, consumo de endpoints, estructura de respuestas, seguridad mediante JWT y consistencia de información.

---

# 2. Alcance

Las pruebas realizadas en esta fase se enfocan en los módulos principales utilizados para la demostración del proyecto:

## Módulos evaluados:

### Autenticación
- Inicio de sesión del usuario administrador.
- Generación y validación de token JWT.

### Residentes
- Consulta de residentes registrados.
- Validación de respuesta del servicio REST.

### Viviendas / Unidades
- Consulta de unidades residenciales.
- Validación de estructura de respuesta.

## Fuera del alcance:

Los siguientes módulos no serán evaluados en esta fase:

- Pagos.
- Cuotas.
- Tickets.
- Visitas.
- Comunicados.
- Reservas.

---

# 3. Objetivos de las pruebas

## Objetivo general

Validar el correcto funcionamiento de la API REST de CondoAdmin mediante pruebas funcionales automatizadas utilizando Postman.

## Objetivos específicos

- Verificar el proceso de autenticación mediante JWT.
- Validar que los endpoints protegidos acepten tokens válidos.
- Comprobar la estructura correcta de las respuestas JSON.
- Verificar códigos HTTP esperados.
- Validar la disponibilidad de los servicios REST principales.
- Generar evidencia objetiva de ejecución para la documentación del proyecto.

---

# 4. Ambiente de pruebas

## Ambiente utilizado

| Parámetro | Valor |
|-|-|
| Tipo de ambiente | Desarrollo desplegado en nube |
| Plataforma | Render Cloud |
| Backend | Spring Boot |
| Base de datos | PostgreSQL |
| API Base URL | https://condominio-api-2aef.onrender.com/api/v1 |
| Cliente de pruebas | Postman |

---

# 5. Arquitectura evaluada

La arquitectura del sistema corresponde a un modelo distribuido:

             API REST
          Spring Boot

                |
                |
    ---------------------------
    |            |            |
    |            |            |

  Web        Desktop       Mobile
React        Python       Flutter

La API REST concentra la lógica de negocio y acceso a datos, mientras que los clientes consumen los servicios mediante solicitudes HTTP autenticadas.

---

# 6. Herramientas utilizadas

## Postman

Herramienta utilizada para:

- Ejecutar solicitudes HTTP.
- Validar respuestas REST.
- Ejecutar scripts automáticos.
- Verificar códigos HTTP.
- Validar estructura JSON.

## Newman

Herramienta complementaria para:

- Ejecutar colecciones Postman desde consola.
- Generar reportes automatizados.

## Swagger/OpenAPI

Utilizado para consultar la documentación disponible de los endpoints REST.

---

# 7. Estrategia de pruebas

Las pruebas siguen el siguiente flujo:


Usuario
|
|
Login
|
|
Obtención JWT
|
|
Consumo endpoints protegidos
|
|
Validación respuesta API


Cada prueba valida:

- Código HTTP.
- Mensaje de respuesta.
- Estructura JSON.
- Datos retornados.

---

# 8. Inventario de endpoints evaluados


# 8.1 Autenticación

Ruta base:


/api/v1/auth


## Endpoint evaluado:

### POST /login

Función:

Permite autenticar usuarios y generar tokens JWT.

---

# Casos de prueba - Autenticación


| ID | Descripción | Precondiciones | Datos Entrada | Resultado Esperado | Resultado Obtenido | Estado |
|-|-|-|-|-|-|-|
| AUTH-001 | Login administrador válido | API disponible | usuario: admin contraseña: password | HTTP 200, success=true, generación JWT | HTTP 200 OK. Login exitoso. AccessToken y RefreshToken generados correctamente | APROBADO |
| AUTH-002 | Login con contraseña incorrecta | Usuario existente | password incorrecto | HTTP 401 Unauthorized | Pendiente | PENDIENTE |
| AUTH-003 | Login con usuario inexistente | API disponible | usuario inválido | HTTP 401 Unauthorized | Pendiente | PENDIENTE |


---

# 8.2 Módulo Residentes

Ruta base:


/api/v1/residentes


Permisos asociados:

- RESIDENTES_LEER
- RESIDENTES_CREAR
- RESIDENTES_EDITAR
- RESIDENTES_ELIMINAR


## Endpoints disponibles:

| Método | Endpoint | Función |
|-|-|-|
| GET | /residentes | Listar residentes |
| GET | /residentes/{id} | Consultar residente |
| POST | /residentes | Crear residente |
| PUT | /residentes/{id} | Actualizar residente |
| DELETE | /residentes/{id} | Eliminar residente |


---

# Casos de prueba - Residentes (Actualizado)

| ID | Descripción del Caso de Prueba | Precondiciones | Datos de Entrada | Resultado Esperado | Resultado Obtenido | Estado |
|---|---|---|---|---|---|---|
| RES-001 | Listar residentes | Usuario autenticado con JWT válido | GET /residentes?page=0&size=20 | HTTP 200 OK y lista de residentes correctamente estructurada | HTTP 200 OK. Lista de residentes retornada correctamente. Tiempo de respuesta: 2.51 segundos. Tamaño: 1.13 KB. Validaciones Postman exitosas (3/3) | APROBADO |
| RES-002 | Crear residente | Usuario autenticado con JWT válido y permiso RESIDENTES_CREAR | Datos completos del residente | HTTP 201 Created y generación de identificador único | HTTP 201 OK. Mensaje: "Persona creada exitosamente". Residente creado correctamente. ID generado: 5 | APROBADO |
| RES-003 | Consultar residente creado | Residente registrado previamente | GET /residentes/5 | HTTP 200 OK con información completa del residente | HTTP 200 OK. Persona encontrada correctamente. Datos del residente Carlos Gomez recuperados correctamente | APROBADO |
| RES-004 | Actualizar residente | Residente existente y permiso RESIDENTES_EDITAR | PUT /residentes/5 con datos modificados | HTTP 200 OK y datos actualizados correctamente | HTTP 200 OK. Mensaje: "Persona actualizada". Teléfono y dirección modificados correctamente | APROBADO |

---

# 8.3 Módulo Viviendas / Unidades


Ruta base:


/api/v1/unidades



Permisos asociados:

- UNIDADES_LEER
- UNIDADES_CREAR
- UNIDADES_EDITAR
- UNIDADES_ELIMINAR


## Endpoints disponibles:


| Método | Endpoint | Función |
|-|-|-|
| GET | /unidades | Listar viviendas |
| GET | /unidades/{id} | Consultar vivienda |
| POST | /unidades | Crear vivienda |
| PUT | /unidades/{id} | Actualizar vivienda |
| DELETE | /unidades/{id} | Eliminar vivienda |


---

# Casos de prueba - Viviendas (Actualizado)

| ID | Descripción del Caso de Prueba | Precondiciones | Datos de Entrada | Resultado Esperado | Resultado Obtenido | Estado |
|---|---|---|---|---|---|---|
| VIV-001 | Listar viviendas | Usuario autenticado con JWT válido | GET /unidades?page=0&size=20 | HTTP 200 OK y lista de unidades correctamente estructurada | HTTP 200 OK. Unidades obtenidas correctamente. Se retornaron 2 registros con información del condominio, estado, número, piso, tipo y alícuota | APROBADO |
| VIV-002 | Crear vivienda | Usuario autenticado con JWT válido y permiso UNIDADES_CREAR | Datos válidos de unidad residencial | HTTP 201 Created y generación de identificador único | HTTP 201 OK. Mensaje: "Unidad creada exitosamente". Unidad creada correctamente. ID generado: 3 | APROBADO |
| VIV-003 | Consultar vivienda creada | Unidad registrada previamente | GET /unidades/3 | HTTP 200 OK con información completa de la unidad | HTTP 200 OK. Mensaje "Unidad encontrada". Datos de la unidad 101-A recuperados correctamente | APROBADO |
| VIV-004 | Actualizar vivienda | Unidad existente y permiso UNIDADES_EDITAR | PUT /unidades/3 con datos modificados | HTTP 200 OK y actualización correcta | HTTP 200 OK. Mensaje "Unidad actualizada". Piso y alícuota modificados correctamente | APROBADO |

---

# 9. Evidencias obtenidas


## AUTH-001

Evidencia:

- Solicitud POST Login.
- Respuesta HTTP 200.
- Generación de JWT.
- Roles del usuario administrador.


## RES-001

Evidencia:

- Solicitud GET Residentes.
- Header Authorization Bearer JWT.
- Respuesta HTTP 200.
- Test Results Postman 3/3 aprobados.

---

# 10. Incidentes Encontrados (Actualizado)

### INC-001 — Error temporal al consultar residentes

| Campo | Resultado |
|---|---|
| Endpoint afectado | GET /api/v1/residentes?page=0&size=20 |
| Código inicial | HTTP 500 Internal Server Error |
| Descripción | Primera ejecución del endpoint retornó error interno del servidor |
| Análisis | Se realizó una segunda ejecución utilizando el mismo flujo de autenticación |
| Resultado final | La petición respondió correctamente con HTTP 200 OK |
| Causa confirmada | No reproducible. Posible comportamiento temporal del ambiente desplegado |
| Estado | CERRADO |

### INC-002 — Token JWT expirado durante ejecución

| Campo | Resultado |
|---|---|
| Endpoint afectado | POST /api/v1/residentes |
| Código inicial | HTTP 401 Unauthorized |
| Descripción | La solicitud de creación de residente fue rechazada por autenticación inválida |
| Causa | Access Token JWT expirado |
| Evidencia | Render Logs: JWT expired 786127 milliseconds ago |
| Análisis | El usuario y permisos eran correctos. El problema correspondía al tiempo de vida del token generado |
| Solución aplicada | Renovación del token mediante nuevo inicio de sesión en /api/v1/auth/login |
| Resultado final | La misma prueba fue ejecutada nuevamente obteniendo HTTP 201 Created |
| Estado | CERRADO |

---

# 11. Resumen final de pruebas API ejecutadas

| ID | Caso | Estado |
|---|---|---|
| AUTH-001 | Login administrador | ✅ APROBADO |
| RES-001 | Listar residentes | ✅ APROBADO |
| RES-002 | Crear residente | ✅ APROBADO |
| RES-003 | Consultar residente | ✅ APROBADO |
| RES-004 | Actualizar residente | ✅ APROBADO |
| VIV-001 | Listar viviendas | ✅ APROBADO |
| VIV-002 | Crear vivienda | ✅ APROBADO |
| VIV-003 | Consultar vivienda | ✅ APROBADO |
| VIV-004 | Actualizar vivienda | ✅ APROBADO |

---

# 12. Conclusión

Las primeras pruebas realizadas evidencian que la API REST de CondoAdmin permite autenticar usuarios mediante JWT y atender solicitudes protegidas correctamente.

El servicio de consulta de residentes fue validado satisfactoriamente, confirmando la comunicación entre Postman y la API desplegada.

Las siguientes etapas estarán enfocadas en completar las pruebas CRUD de residentes y viviendas, generando las evidencias correspondientes.
