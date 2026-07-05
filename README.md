# Condominio API

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16%2B-336791?logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-10%2B-CC0200?logo=flyway&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-OAS_3-85EA2D?logo=swagger&logoColor=black)
![JWT](https://img.shields.io/badge/JWT-Security-black?logo=jsonwebtokens)

API RESTful para el Sistema de Gestión de Condominios. Provee todos los servicios de negocio (Autenticación, Gestión de Residentes, Pagos, Multas, Asambleas, Comunicación y Seguridad en Garita) para ser consumidos por los clientes Web, Móvil y de Escritorio.

## Tecnologías Utilizadas

- **Lenguaje**: Java 21
- **Framework Core**: Spring Boot 3.x
- **Persistencia**: Spring Data JPA / Hibernate
- **Base de Datos**: PostgreSQL 16+
- **Migraciones**: Flyway
- **Seguridad**: Spring Security con JWT (JSON Web Tokens)
- **Mapeo de DTOs**: MapStruct
- **Documentación API**: OpenAPI 3 (Swagger UI)
- **Pruebas**: JUnit 5, Mockito, JaCoCo (Cobertura de Código)

## Requisitos Previos

- **Java 21** instalado en la máquina (JDK).
- **Maven 3.8+** instalado.
- **PostgreSQL 16+** corriendo en el puerto 5432.

## Configuración y Ejecución

### 1. Crear la Base de Datos

Entra a tu servidor local de PostgreSQL (`psql` o pgAdmin) y crea una base de datos vacía:

```sql
CREATE DATABASE condominio_db;
```

### 2. Configurar Variables de Entorno

El proyecto usa variables de entorno para su configuración. Crea un archivo `.env` en la raíz del proyecto (puedes basarte en `.env.example` si existe) o exporta las siguientes variables en tu terminal:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/condominio_db
export DB_USERNAME=tu_usuario_postgres
export DB_PASSWORD=tu_password_postgres
export JWT_SECRET=una_clave_secreta_muy_larga_y_segura_para_firmar_tokens_jwt
export JWT_EXPIRATION=86400000
export JWT_REFRESH_EXPIRATION=604800000
```

> **Nota:** Si ejecutas desde un IDE (IntelliJ, VSCode), asegúrate de inyectar estas variables en la configuración de ejecución (Run Configuration).

### 3. Ejecutar Migraciones (Flyway) y Semillas

Flyway está integrado en el ciclo de vida de Spring Boot. Al arrancar la aplicación, automáticamente ejecutará todos los scripts SQL ubicados en `src/main/resources/db/migration/`. 

Esto incluye:
- `V1__schema.sql`: Estructura completa de la base de datos (más de 30 tablas).
- Archivos `V2` a `V5`: Constraints, Índices, Triggers y Funciones de PostgreSQL.
- Archivos Seed (`V6__seed.sql`, `V8__test_auth_seed.sql`, etc.): Datos iniciales y usuarios administradores por defecto.

No necesitas correr scripts SQL manualmente.

### 4. Levantar el Proyecto

Abre tu terminal en la raíz del proyecto y ejecuta:

```bash
mvn clean install
mvn spring-boot:run
```

El servidor arrancará en `http://localhost:8080`.

## Documentación y Swagger

Toda la API está documentada dinámicamente con OpenAPI 3. Una vez levantado el servidor, accede a la interfaz gráfica de Swagger en:

👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

Desde ahí puedes explorar todos los módulos (Auth, Residentes, Departamentos, Pagos, Multas, Tickets, Asambleas, Actas, Comunicados, etc.) y probar los endpoints.

## Credenciales Iniciales (Seed)

El script de semilla carga un súper-administrador por defecto para que puedas entrar al sistema inmediatamente y obtener tu token JWT.

- **Correo**: `admin@condominio.com`
- **Contraseña**: `Admin123!`

**Para probar los endpoints protegidos en Swagger:**
1. Ve al endpoint de Auth (`POST /api/v1/auth/login`) y usa las credenciales de arriba.
2. Copia el `accessToken` que te devuelve.
3. En la parte superior de la página de Swagger, dale click al botón verde **"Authorize"**.
4. Pega tu token en el formato: `Bearer tu_token_largo_aqui` y dale a "Authorize".
5. ¡Listo! Ya tienes acceso a todos los endpoints bloqueados como `ADMIN`.

## Arquitectura Limpia

El proyecto sigue una estructura limpia dividida por capas para asegurar mantenibilidad y escalabilidad:

- `controller/`: Expone las rutas RESTful. Solo recibe DTOs y devuelve DTOs (nada de entidades).
- `service/`: Contiene `interfaces/` y sus implementaciones (`impl/`). Aquí reside toda la lógica de negocio y las validaciones.
- `repository/`: Interfaces de Spring Data JPA (consultas a base de datos).
- `entity/`: Clases de dominio mapeadas a las tablas de PostgreSQL mediante anotaciones JPA (`@Entity`).
- `dto/`: Objetos de transferencia de datos (`request/` y `response/`), asegurando que no sobre-exponemos el dominio interno.
- `mapper/`: Interfaces de MapStruct que transforman de Entity a DTO y viceversa automáticamente.
- `security/`: Configuración de Spring Security, filtros de validación de JWT, etc.
- `audit/`: Interceptores de Hibernate/Postgres (`PostgresAuditInterceptor`) para inyectar automáticamente el usuario logueado en la base de datos durante las inserciones.
- `exception/`: Manejador global de excepciones (`GlobalExceptionHandler`) para devolver siempre un formato JSON estándar de error (`ApiResponse`).
- `config/`: Clases de configuración (Swagger, CORS, JPA).

## Contribuir y Desarrollar

- **No comitees archivos basura:** Asegúrate de respetar el `.gitignore` (no subir `target/`, archivos del IDE como `.idea/` o `.vscode/`, ni logs locales).
- **Pruebas (Tests):** El proyecto cuenta con pruebas unitarias robustas que cubren capas de servicio. Ejecútalas con `mvn test`. Jacoco generará reportes de cobertura en `target/site/jacoco/index.html`.
