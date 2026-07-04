# CONVENTIONS.md — condominio-api
> Guía obligatoria para todo desarrollador que contribuya al proyecto.
> **Última revisión:** 2026-07-04 · **Versión API:** v1

---

## Índice
1. [Decisiones de Arquitectura](#1-decisiones-de-arquitectura)
2. [Nomenclatura](#2-nomenclatura)
3. [Estructura de Paquetes](#3-estructura-de-paquetes)
4. [Endpoints REST](#4-endpoints-rest)
5. [DTOs Request / Response](#5-dtos-request--response)
6. [Validaciones](#6-validaciones)
7. [ApiResponse — Formato de Respuesta](#7-apiresponse--formato-de-respuesta)
8. [Transacciones](#8-transacciones)
9. [Logging](#9-logging)
10. [Paginación y Ordenamiento](#10-paginación-y-ordenamiento)
11. [Pruebas](#11-pruebas)
12. [Versionado de la API](#12-versionado-de-la-api)
13. [MapStruct](#13-mapstruct)
14. [Nombres de Métodos](#14-nombres-de-métodos)
15. [Entidades JPA](#15-entidades-jpa)

---

## 1. Decisiones de Arquitectura

Estas decisiones son **definitivas**. No se reabren sin consenso del equipo.

### 1.1 UUID — Solo donde aporta valor real

UUID **solo** se usa en:
- Tokens de recuperación de contraseña
- Links de invitación
- Recursos públicos compartidos (sin autenticación)

**No se usa** en: `Usuario`, `Persona`, `Rol`, `Permiso`, `Condominio`, `Torre`, `Unidad`, ni ninguna entidad de dominio.

**Razón:** Todo acceso pasa por JWT. La autorización se valida en cada endpoint. UUID agrega columnas, índices, mappers y búsquedas innecesarias sin beneficio real en un sistema cerrado.

```java
// ✅ CORRECTO
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

// ❌ PROHIBIDO en entidades de dominio
private UUID uuid;
```

### 1.2 Sin BaseAuditEntity — Auditoría delegada a PostgreSQL

La auditoría la gestiona **exclusivamente PostgreSQL** mediante:
- Triggers `BEFORE INSERT / UPDATE` definidos en condominio-db
- Variable de sesión `app.usuario_actual` inyectada por `PostgresAuditInterceptor`
- Tabla `auditoria` en la base de datos

Las entidades JPA **no** extienden ninguna clase base de auditoría. Las columnas de auditoría se mapean como `insertable = false, updatable = false`.

```java
// ✅ CORRECTO
@Column(name = "created_at", insertable = false, updatable = false)
private LocalDateTime createdAt;

@Column(name = "updated_at", insertable = false, updatable = false)
private LocalDateTime updatedAt;

// ❌ PROHIBIDO
public class Rol extends BaseAuditEntity { ... }
```

### 1.3 @EntityGraph — Solo como último recurso

| Caso de uso | Herramienta |
|---|---|
| Buscar por ID o campo simple | Método derivado de `JpaRepository` |
| Filtros o condiciones | `@Query` con JPQL |
| JOIN de 2+ asociaciones en un caso concreto | `@Query` con `JOIN FETCH` |
| Grafo complejo reutilizable en múltiples queries | `@EntityGraph` |

Un repositorio **no debe tener más de 2 métodos con `@EntityGraph`**. Si hay más, revisar el diseño.

---

## 2. Nomenclatura

### Entidades JPA
- `PascalCase` singular en español.
- `@Table(name = "snake_case_plural")` siempre explícito.

| Entidad Java | Tabla PostgreSQL |
|---|---|
| `Rol` | `roles` |
| `Permiso` | `permisos` |
| `Usuario` | `usuarios` |
| `UsuarioRol` | `usuario_roles` |
| `Persona` | `personas` |
| `Condominio` | `condominios` |
| `Torre` | `torres` |
| `Unidad` | `unidades` |
| `PersonaUnidad` | `persona_unidades` |
| `RolPermiso` | `rol_permisos` |

### DTOs

| Sufijo | Propósito |
|---|---|
| `{Entidad}Request` | Entrada para crear / actualizar |
| `{Entidad}Response` | Salida completa |
| `{Entidad}SummaryResponse` | Salida reducida para listas / combos |
| `{Entidad}PageResponse` | Wrapper para respuestas paginadas si se necesita |

### Clases por capa

| Capa | Sufijo | Ejemplo |
|---|---|---|
| Entidad | ninguno | `Rol` |
| Repositorio | `Repository` | `RolRepository` |
| Servicio (interfaz) | `Service` | `RolService` |
| Servicio (impl) | `ServiceImpl` | `RolServiceImpl` |
| Controlador | `Controller` | `RolController` |
| Mapper | `Mapper` | `RolMapper` |

---

## 3. Estructura de Paquetes

```
com.condominio.condominio_api/
│
├── config/                   # Beans de configuración (Swagger, CORS, JPA)
├── security/                 # Filtros JWT, Entry Point, Access Denied Handler
├── controller/               # @RestController — solo delegan al servicio
├── service/
│   ├── interfaces/           # Contratos (@Transactional declarado aquí)
│   └── impl/                 # Lógica de negocio
├── repository/               # JpaRepository
├── entity/                   # Entidades JPA
├── dto/
│   ├── request/              # Objetos de entrada
│   └── response/             # Objetos de salida
├── mapper/                   # MapStruct mappers
├── exception/                # Excepciones de dominio + GlobalExceptionHandler
├── validation/               # Validadores personalizados (@Component)
└── util/                     # Utilidades estáticas (ApiResponseUtil)
```

**Reglas absolutas:**
- `controller/` solo delega — sin lógica de negocio ni acceso directo al repositorio.
- `entity/` sin referencias a DTOs ni clases de servicio.
- `dto/` sin referencias a entidades JPA.
- Toda la lógica de negocio vive en `service/impl/`.

---

## 4. Endpoints REST

### URL base
```
/api/v1/{recurso-plural-kebab-case}
```

### Convenciones de URL

- Sustantivos plurales, **sin verbos**: ~~`/api/v1/obtenerRoles`~~
- Kebab-case: `/api/v1/usuario-roles`, `/api/v1/rol-permisos`
- Subrecursos para jerarquías claras:
  ```
  GET /api/v1/condominios/{id}/torres
  GET /api/v1/torres/{id}/unidades
  ```

### Métodos HTTP estándar

| Operación | Método | URL | Código |
|---|---|---|---|
| Listar paginado | GET | `/api/v1/roles` | 200 |
| Obtener por ID | GET | `/api/v1/roles/{id}` | 200 |
| Crear | POST | `/api/v1/roles` | 201 |
| Actualizar completo | PUT | `/api/v1/roles/{id}` | 200 |
| Actualizar parcial | PATCH | `/api/v1/roles/{id}` | 200 |
| Eliminar | DELETE | `/api/v1/roles/{id}` | 204 |

### Anotaciones obligatorias en cada controller
```java
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Gestión de roles del sistema")
public class RolController {

    @PreAuthorize("hasRole('ADMIN')")          // OBLIGATORIO en cada método
    @GetMapping("/{id}")
    @Operation(summary = "Obtener rol por ID")
    public ResponseEntity<ApiResponse<RolResponse>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseUtil.ok("Rol encontrado", rolService.findById(id)));
    }
}
```

---

## 5. DTOs Request / Response

### Request — usar `@Data`
```java
@Data
@NoArgsConstructor
public class RolRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String nombre;

    @Size(max = 200)
    private String descripcion;
}
```

### Response — usar `@Getter @Builder`
```java
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private LocalDateTime createdAt;
}
```

**Reglas:**
- Request: `@Data` (necesita setters para deserialización por Jackson).
- Response: `@Getter @Builder` (inmutable desde el servidor).
- **Nunca** exponer la entidad JPA como respuesta de un endpoint.
- **Nunca** recibir una entidad JPA como parámetro de un controller.

---

## 6. Validaciones

### Anotaciones en DTOs
```java
@NotNull       // el campo no puede ser null
@NotBlank      // no null, no vacío, no solo espacios (Strings)
@NotEmpty      // no null, no vacío (colecciones y Strings)
@Size(min, max)
@Email
@Pattern(regexp = "...")
@Positive
@Min / @Max
```

### En controllers
```java
// @Valid en el body
public ResponseEntity<?> crear(@Valid @RequestBody RolRequest request) { }

// @Validated en la clase para validar parámetros de path/query
@Validated
public class RolController { }
```

### Validaciones de negocio → siempre en el servicio
```java
// ✅ CORRECTO — RolServiceImpl
if (rolRepository.existsByNombreIgnoreCase(request.getNombre())) {
    throw new ResourceAlreadyExistsException("Rol", "nombre", request.getNombre());
}

// ❌ PROHIBIDO — en RolController
```

### Validadores personalizados
Ubicar en `validation/`. Crear la anotación y el `ConstraintValidator` separados:
```
validation/
├── RucValido.java          ← anotación
├── RucValidator.java       ← implementación ConstraintValidator
```

---

## 7. ApiResponse — Formato de Respuesta

### Estructura JSON estándar
```json
{
  "success": true,
  "status": 200,
  "message": "Operación exitosa",
  "data": { },
  "timestamp": "2026-07-04 14:30:00",
  "errors": null
}
```

### Uso en controllers
```java
return ResponseEntity.ok(ApiResponseUtil.ok("Rol encontrado", response));
return ResponseEntity.status(201).body(ApiResponseUtil.created("Rol creado", response));
return ResponseEntity.noContent().build();  // DELETE
```

### Formato de errores de validación
```json
{
  "success": false,
  "status": 400,
  "message": "Error de validación",
  "errors": [
    { "field": "nombre", "message": "El nombre es obligatorio" }
  ]
}
```

---

## 8. Transacciones

### Declarar en la interfaz del servicio, no en la implementación
```java
public interface RolService {

    @Transactional(readOnly = true)
    RolResponse findById(Long id);

    @Transactional(readOnly = true)
    Page<RolResponse> findAll(Pageable pageable);

    @Transactional
    RolResponse create(RolRequest request);

    @Transactional
    RolResponse update(Long id, RolRequest request);

    @Transactional
    void delete(Long id);
}
```

**Reglas:**
- `readOnly = true` en **todos** los métodos de consulta, sin excepción.
- `@Transactional` nunca en `@Controller` ni en `@Repository`.
- Propagación `REQUIRED` (default) para la mayoría de casos.
- `REQUIRES_NEW` solo para transacciones verdaderamente independientes (ej. log de error).
- El `PostgresAuditInterceptor` ejecuta `SET LOCAL app.usuario_actual = '...'` al inicio de cada transacción de escritura para alimentar los triggers de PostgreSQL.

---

## 9. Logging

```java
// ✅ Usar @Slf4j de Lombok — nunca instanciar Logger manualmente
@Slf4j
@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService { }
```

### Niveles

| Nivel | Cuándo |
|---|---|
| `ERROR` | Excepción no controlada, fallo de integración externa |
| `WARN` | Situación anómala recuperable (recurso no encontrado, acceso denegado) |
| `INFO` | Operaciones de escritura completadas (crear, actualizar, eliminar) |
| `DEBUG` | Parámetros de entrada/salida durante desarrollo |
| `TRACE` | SQL de Hibernate, solo en perfiles de diagnóstico |

```java
// ✅ CORRECTO — incluir contexto, sin datos sensibles
log.info("Rol creado: id={}, nombre={}", rol.getId(), rol.getNombre());
log.warn("Rol no encontrado: id={}", id);
log.error("Error al eliminar rol id={}: {}", id, e.getMessage(), e);

// ❌ PROHIBIDO — datos sensibles
log.debug("Login: usuario={}, password={}", username, password);
```

---

## 10. Paginación y Ordenamiento

### Parámetros de query
```
GET /api/v1/roles?page=0&size=20&sort=nombre,asc
GET /api/v1/usuarios?page=0&size=10&sort=id,desc
```

### En el controller
```java
@GetMapping
public ResponseEntity<ApiResponse<Page<RolResponse>>> listar(
        @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
        Pageable pageable) {
    return ResponseEntity.ok(ApiResponseUtil.ok("Roles", rolService.findAll(pageable)));
}
```

**Reglas:**
- `size` máximo: **100** (validar en el servicio).
- `size` por defecto: **20**.
- Orden por defecto: `id ASC`.
- Para listas de selección (combos): usar `List<{Entidad}SummaryResponse>` sin paginación.

---

## 11. Pruebas

### Estructura
```
src/test/java/com/condominio/condominio_api/
├── unit/
│   ├── service/        ← Mockito, sin contexto Spring
│   └── mapper/         ← Tests de MapStruct
└── integration/
    ├── controller/     ← MockMvc + @WebMvcTest
    └── repository/     ← @DataJpaTest + Testcontainers
```

### Nombre de métodos
```java
// Formato: should{Resultado}_when{Condicion}
void shouldReturnRol_whenIdExists()
void shouldThrowNotFoundException_whenIdNotExists()
void shouldReturn201_whenRolCreatedSuccessfully()
```

### Test unitario de servicio
```java
@ExtendWith(MockitoExtension.class)
class RolServiceImplTest {
    @Mock RolRepository rolRepository;
    @Mock RolMapper rolMapper;
    @InjectMocks RolServiceImpl rolService;

    @Test
    void shouldReturnRol_whenIdExists() {
        // given
        var rol = new Rol();
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(rolMapper.toResponse(rol)).thenReturn(RolResponse.builder().id(1L).build());
        // when
        var result = rolService.findById(1L);
        // then
        assertThat(result).isNotNull();
        verify(rolRepository).findById(1L);
    }
}
```

### Test de controller
```java
@WebMvcTest(RolController.class)
@AutoConfigureMockMvc(addFilters = false)
class RolControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean RolService rolService;

    @Test
    void shouldReturn200_whenListingRoles() throws Exception {
        mockMvc.perform(get("/api/v1/roles"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true));
    }
}
```

### Cobertura mínima
| Capa | Cobertura |
|---|---|
| `service/impl/` | 80% |
| `controller/` | 70% |
| Queries `@Query` custom | 100% |

---

## 12. Versionado de la API

**Estrategia: versionado por URL** (`/api/v1/`, `/api/v2/`)

### Cuándo crear v2
Solo ante **breaking changes**:
- Cambio en estructura del response que rompe clientes actuales.
- Cambio en contrato de autenticación.
- Renombrado de campos obligatorios.

### Cuándo NO crear v2
- Agregar campo opcional al response → retrocompatible.
- Agregar endpoint nuevo → sin impacto.
- Agregar parámetro opcional de query → retrocompatible.

**v1 se mantiene mínimo 6 meses después de publicar v2.**

---

## 13. MapStruct

```java
@Mapper(componentModel = "spring")
public interface RolMapper {

    RolResponse toResponse(Rol rol);

    Rol toEntity(RolRequest request);

    List<RolResponse> toResponseList(List<Rol> roles);

    // Para actualizaciones: ignorar campos null del request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(RolRequest request, @MappingTarget Rol rol);
}
```

**Reglas:**
- Un mapper por entidad de dominio.
- Usar `@Mapping(target = "x", ignore = true)` para campos no mapeables.
- Usar `@Mapping(source = "entidad.campo", target = "campoDto")` para anidados.
- Lógica post-mapeo en `@AfterMapping`, no en el servicio.
- **Nunca** convertir manualmente en el servicio lo que MapStruct puede hacer.

---

## 14. Nombres de Métodos

### Repositorios
```java
// Métodos derivados (Spring Data)
Optional<Rol> findByNombre(String nombre);
boolean existsByNombreIgnoreCase(String nombre);
List<Rol> findByActivoTrue();
Page<Rol> findByActivoTrue(Pageable pageable);

// JPQL con @Query
@Query("SELECT r FROM Rol r WHERE r.activo = true ORDER BY r.nombre")
List<Rol> findAllActive();

@Query("SELECT r FROM Rol r JOIN FETCH r.rolPermisos rp JOIN FETCH rp.permiso WHERE r.id = :id")
Optional<Rol> findByIdWithPermisos(@Param("id") Long id);
```

### Servicios
```java
// Lecturas
RolResponse findById(Long id);
Page<RolResponse> findAll(Pageable pageable);
List<RolSummaryResponse> findAllActive();

// Escrituras
RolResponse create(RolRequest request);
RolResponse update(Long id, RolRequest request);
void delete(Long id);

// Operaciones de dominio
void assignPermiso(Long rolId, Long permisoId);
void revokePermiso(Long rolId, Long permisoId);
void activate(Long id);
void deactivate(Long id);
```

### Controladores
```java
@GetMapping           → listar()
@GetMapping("/{id}")  → obtenerPorId()
@PostMapping          → crear()
@PutMapping("/{id}")  → actualizar()
@PatchMapping         → activar() / desactivar()
@DeleteMapping("/{id}") → eliminar()
```

---

## 15. Entidades JPA

### Template base
```java
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"rolPermisos"})
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", unique = true, nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    // Escritas por trigger de PostgreSQL — solo lectura desde JPA
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "rol",
               cascade = {CascadeType.PERSIST, CascadeType.MERGE},
               fetch = FetchType.LAZY)
    private List<RolPermiso> rolPermisos = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rol other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() { return getClass().hashCode(); }
}
```

### Prohibiciones absolutas
- `@Data` en entidades JPA
- `@ManyToMany` directo
- `fetch = FetchType.EAGER`
- `extends BaseAuditEntity`
- Referencias a DTOs dentro de la entidad
- Lógica de negocio en getters/setters

---

## Orden de implementación de entidades

```
1.  Permiso          ← sin dependencias
2.  Rol              ← sin dependencias
3.  RolPermiso       ← Rol + Permiso
4.  Usuario          ← sin dependencias de dominio
5.  UsuarioRol       ← Usuario + Rol
6.  Persona          ← sin dependencias de dominio
7.  Condominio       ← sin dependencias
8.  Torre            ← Condominio
9.  Unidad           ← Torre
10. PersonaUnidad    ← Persona + Unidad
```

Después de implementar `Usuario` + `UsuarioRol`:
→ Reemplazar `UserDetailsConfig` (in-memory) con `UserDetailsServiceImpl` real basado en BD.
