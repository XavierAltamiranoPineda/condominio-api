# 🏢 Condominio API

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16%2B-336791?logo=postgresql&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-success?logo=springsecurity)
![Flyway](https://img.shields.io/badge/Flyway-Database_Migrations-red?logo=flyway)
![Swagger](https://img.shields.io/badge/OpenAPI-Swagger-85EA2D?logo=swagger)

Backend REST corporativo desarrollado con **Spring Boot** para la gestión integral de condominios. El sistema centraliza la administración financiera, operativa y comunitaria, brindando una arquitectura robusta, auditable y escalable basada en los principios de Clean Architecture.

Está diseñado para servir como el cerebro (API de servicios) para múltiples interfaces cliente: **Aplicaciones Web, Móviles (Flutter) y de Escritorio**.

---

## 📌 Características Principales

El proyecto aborda cada aspecto crítico de la vida en un condominio:

- 🛡️ **Seguridad y Control de Acceso:** Gestión en garita, preautorización de visitas con evidencias (fotos) y rol de `GUARDIA`.
- 💰 **Motor Financiero:** Emisión de cuotas ordinarias/extraordinarias, registro de pagos, recibos en PDF, multas enlazadas a cuotas y refinanciamiento a través de convenios de pago a plazos.
- 🗳️ **Gobernanza y Asambleas:** Gestión de juntas virtuales, control estricto de quórum, prevención de voto doble y actas con evidencias documentales.
- 🛠️ **Operaciones y Mantenimiento:** Sistema de Tickets (Mesa de Ayuda) para incidencias, asignación de prioridades y categorías dinámicas.
- 📢 **Comunicación Efectiva:** Tablón de comunicados segmentado (por torre, rol o unidad), notificaciones (push/email) con seguimiento y confirmación de lectura.
- 🏢 **Gestión Patrimonial:** Control de condominios, torres, departamentos, áreas comunes y asignación de parqueaderos/vehículos.
- 🕵️ **Auditoría Avanzada:** Registro automático (nivel base de datos) de quién realizó cambios (insert/update/delete) en cada tabla usando Triggers e Interceptores JPA.

---

## 🛠 Tecnologías

| Tecnología | Rol en el Proyecto |
|------------|----------|
| **Java 21** | Lenguaje Core (Records, Pattern Matching) |
| **Spring Boot 3.x** | Framework principal |
| **Spring Security (JWT)**| Autenticación, Refresh Tokens y RBAC (Control de Acceso) |
| **Spring Data JPA** | Persistencia y consultas a BD (Hibernate) |
| **PostgreSQL 16+** | Base de datos relacional y motor de auditoría |
| **Flyway** | Versionado de la BD (Migraciones automáticas) |
| **MapStruct** | Mapeo ultra-rápido entre Entidades y DTOs |
| **Maven** | Gestor de dependencias y empaquetado |
| **Swagger / OpenAPI 3** | Documentación de endpoints en vivo |
| **JUnit 5 / Mockito** | Pruebas Unitarias y Mocks |
| **JaCoCo** | Análisis de Cobertura de Código |

---

## 🏗 Arquitectura

El proyecto sigue una arquitectura limpia basada en capas estandarizadas. La lógica de negocio está completamente aislada de los detalles de persistencia y exposición web.

```text
 🌐 Controller (Recibe DTOs / Valida / Responde ApiResponse)
       │
       ▼
 ⚙️ Service (Lógica de Negocio / Validaciones Complejas / MapStruct)
       │
       ▼
 🗄️ Repository (Spring Data JPA / Consultas JPQL Nativas)
       │
       ▼
 🐘 PostgreSQL (Reglas de Integridad / Triggers / Funciones Nativas)
```

**Módulos transversales (Cross-Cutting Concerns):**
- `Security`: Filtros JWT, inyección de contexto y `@PreAuthorize`.
- `Audit`: Interceptor `PostgresAuditInterceptor` que inyecta automáticamente el ID del usuario en sesión hacia Postgres para los triggers de auditoría.
- `Exception Handler`: `@ControllerAdvice` para garantizar un formato JSON estándar unificado ante cualquier fallo.

---

## 📂 Estructura del Código

```text
src
 ├── controller           # Endpoints de la API RESTful (Swagger-annotated)
 ├── service
 │     ├── interfaces     # Contratos de los servicios
 │     └── impl           # Lógica pura del negocio
 ├── repository           # Interfaces JpaRepository
 ├── entity               # Modelos mapeados a tablas SQL (@Entity)
 ├── dto
 │     ├── request        # Inputs validados (@Valid, @NotNull)
 │     └── response       # Outputs limpios (Sin exponer lógica interna)
 ├── mapper               # MapStruct interfaces
 ├── security             # JWT, UserDetails, filtros web
 ├── audit                # Interceptores JPA-to-PostgreSQL
 ├── config               # Configuración global (CORS, Swagger, JPA)
 ├── exception            # Excepciones custom y manejador global
 └── resources
       └── db
            └── migration # Migraciones Flyway (V1..V8)
```

---

## 📋 Requisitos Previos

- Java 21+ (JDK)
- Maven 3.9+
- PostgreSQL 16+
- Git

---

## ⚙ Configuración del Entorno

1. Crear la base de datos en PostgreSQL:

```sql
CREATE DATABASE condominio_db;
```

2. Configurar las variables de entorno. Puedes crear un archivo `.env` o configurarlo en tu IDE:

```properties
# Base de Datos
DB_URL=jdbc:postgresql://localhost:5432/condominio_db
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password

# Seguridad JWT
JWT_SECRET=escribe_aqui_una_clave_secreta_muy_segura_y_extensa
JWT_EXPIRATION=86400000          # 1 día (ms)
JWT_REFRESH_EXPIRATION=604800000 # 7 días (ms)
```

---

## 🚀 Ejecución

**1. Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/condominio-api.git
cd condominio-api
```

**2. Compilar e instalar dependencias**
```bash
mvn clean install
```

**3. Levantar el servidor**
```bash
mvn spring-boot:run
```

La API estará lista y escuchando en `http://localhost:8080`.

---

## 🗄 Base de Datos y Migraciones

Este proyecto implementa **Flyway**. Al iniciar `spring-boot:run`, Flyway lee la carpeta `src/main/resources/db/migration/` y crea toda la arquitectura SQL automáticamente:

- `V1`: Esquema relacional central (Entidades).
- `V2 - V3`: Constraints (Restricciones lógicas) e Índices.
- `V4 - V5`: Triggers y Funciones (Lógica de auditoría en la BD).
- `V6 - V8`: Data Seed (Semilla de roles, permisos, categorías y un súper usuario).

¡No tienes que ejecutar scripts manuales ni preocuparte por la sincronización del esquema!

---

## 🔐 Usuario de Pruebas

Para facilitarte la vida, Flyway inserta un Administrador raíz al arrancar la base de datos vacía por primera vez:

- **Correo:** `admin@condominio.com`
- **Contraseña:** `Admin123!`

Úsalo en el endpoint `POST /api/v1/auth/login` para recibir tu Access Token.

---

## 📖 Documentación (Swagger)

La API entera es auto-documentada usando OpenAPI 3.

🔗 **[Ver Swagger UI en Local](http://localhost:8080/swagger-ui/index.html)**

**Cómo probar endpoints privados:**
1. Haz login para obtener el JWT.
2. Copia la propiedad `accessToken`.
3. Haz clic en el botón verde **"Authorize"** en la parte superior de Swagger.
4. Escribe `Bearer <tu-token>` y valida.

---

## 🧪 Pruebas y Cobertura

El proyecto está fuertemente testeado para garantizar la estabilidad del negocio (TDD en servicios críticos).

```bash
# Ejecutar toda la batería de tests unitarios y de integración
mvn clean test
```

**Reporte de Cobertura (JaCoCo)**
Se genera automáticamente al correr los tests. Ábrelo en tu navegador:
```text
target/site/jacoco/index.html
```

*Estado aproximado de cobertura:* Clases > 95% | Métodos > 85% | Líneas > 85%.

---

## 📈 Estado del Proyecto

✅ **Backend Feature Complete**

La arquitectura Backend está cerrada y estable. Contiene toda la validación relacional, de permisos, negocio financiero, operativa de guardias y auditoría lista para su consumo directo. El siguiente paso en la vida de este proyecto es la construcción y conexión de los clientes **Frontend (Flutter Mobile, React Web)**.

---

## 👨‍💻 Autor

**Xavier Altamirano**  
*Ingeniería de Software*  
*Universidad de las Fuerzas Armadas ESPE*
