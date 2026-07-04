-- ============================================================
-- V6__seed.sql
-- Datos semilla iniciales requeridos para el funcionamiento del
-- sistema (catálogos estáticos y configuración base).
-- ============================================================

-- ---------- CATÁLOGOS ----------
INSERT INTO estado_unidad (nombre) VALUES
    ('DISPONIBLE'), ('OCUPADA'), ('EN_REFORMA'), ('MANTENIMIENTO');

INSERT INTO estado_reserva (nombre) VALUES
    ('PENDIENTE_APROBACION'), ('APROBADA'), ('RECHAZADA'), ('CANCELADA'), ('COMPLETADA');

INSERT INTO estado_pago (nombre) VALUES
    ('PENDIENTE_APROBACION'), ('CONFIRMADO'), ('RECHAZADO'), ('ANULADO');

INSERT INTO estado_acceso (nombre) VALUES
    ('PREAUTORIZADO'), ('EN_CURSO'), ('FINALIZADO'), ('DENEGADO');

INSERT INTO estado_ticket (nombre) VALUES
    ('ABIERTO'), ('ASIGNADO'), ('EN_PROGRESO'), ('RESOLVIENDO'), ('CERRADO'), ('REAPERTURADO');

-- ---------- CONFIGURACIÓN POR DEFECTO ----------
INSERT INTO configuracion (clave, valor) VALUES
    ('DIAS_MORA', '10'),
    ('PORCENTAJE_INTERES', '5.5'),
    ('VALOR_CUOTA_BASE', '45.00'),
    ('DIAS_EXPIRACION_TOKEN_RECUPERACION', '1'),
    ('MAX_INTENTOS_LOGIN', '5'),
    ('MINUTOS_BLOQUEO_LOGIN', '15');

-- ---------- ROLES DEL SISTEMA ----------
INSERT INTO rol (nombre, descripcion) VALUES
    ('ADMIN', 'Administrador total con acceso a todos los módulos y configuración del sistema'),
    ('GUARDIA', 'Guardia de seguridad con acceso exclusivo a control de accesos y visitas'),
    ('RESIDENTE', 'Residente del condominio con acceso a pagos, incidentes, reservas y comunicados'),
    ('TESORERO', 'Tesorero encargado de la gestión de cobros, multas y aprobación de pagos');

-- ---------- PERMISOS DEL SISTEMA ----------
INSERT INTO permiso (nombre, modulo, accion) VALUES
    -- Residentes (Personas)
    ('Ver residentes', 'RESIDENTES', 'LEER'),
    ('Crear residente', 'RESIDENTES', 'CREAR'),
    ('Editar residente', 'RESIDENTES', 'EDITAR'),
    ('Eliminar residente', 'RESIDENTES', 'ELIMINAR'),

    -- Unidades
    ('Ver unidades', 'UNIDADES', 'LEER'),
    ('Crear unidad', 'UNIDADES', 'CREAR'),
    ('Editar unidad', 'UNIDADES', 'EDITAR'),
    ('Eliminar unidad', 'UNIDADES', 'ELIMINAR'),

    -- Cuotas
    ('Ver cuotas', 'CUOTAS', 'LEER'),
    ('Crear cuota', 'CUOTAS', 'CREAR'),
    ('Editar cuota', 'CUOTAS', 'EDITAR'),
    ('Eliminar cuota', 'CUOTAS', 'ELIMINAR'),

    -- Pagos
    ('Ver pagos', 'PAGOS', 'LEER'),
    ('Registrar pago', 'PAGOS', 'CREAR'),
    ('Aprobar/Rechazar pago', 'PAGOS', 'EDITAR'),
    ('Anular pago', 'PAGOS', 'ELIMINAR'),

    -- Tickets (Soporte/Mantenimiento)
    ('Ver tickets', 'TICKETS', 'LEER'),
    ('Crear ticket', 'TICKETS', 'CREAR'),
    ('Asignar técnico/actualizar estado', 'TICKETS', 'EDITAR'),
    ('Eliminar ticket', 'TICKETS', 'ELIMINAR'),

    -- Reservas de áreas comunes
    ('Ver reservas', 'RESERVAS', 'LEER'),
    ('Crear reserva', 'RESERVAS', 'CREAR'),
    ('Aprobar/Rechazar reserva', 'RESERVAS', 'EDITAR'),
    ('Eliminar reserva', 'RESERVAS', 'ELIMINAR'),

    -- Accesos (Guardias de seguridad)
    ('Ver accesos', 'VISITAS', 'LEER'),
    ('Registrar ingreso/salida', 'VISITAS', 'CREAR'),
    ('Preautorizar visita', 'VISITAS', 'EDITAR'),
    ('Denegar ingreso', 'VISITAS', 'ELIMINAR'),

    -- Comunicados
    ('Ver comunicados', 'COMUNICADOS', 'LEER'),
    ('Crear comunicado', 'COMUNICADOS', 'CREAR'),
    ('Editar comunicado', 'COMUNICADOS', 'EDITAR'),
    ('Eliminar comunicado', 'COMUNICADOS', 'ELIMINAR'),

    -- Asambleas y Votaciones
    ('Ver asambleas', 'ASAMBLEAS', 'LEER'),
    ('Crear asamblea/votación', 'ASAMBLEAS', 'CREAR'),
    ('Registrar voto', 'ASAMBLEAS', 'EDITAR'),
    ('Publicar acta', 'ASAMBLEAS', 'ELIMINAR'),

    -- Reportes
    ('Generar reportes financieros/gestión', 'REPORTES', 'LEER'),

    -- Usuarios del Sistema
    ('Ver usuarios', 'USUARIOS', 'LEER'),
    ('Crear usuario', 'USUARIOS', 'CREAR'),
    ('Editar usuario', 'USUARIOS', 'EDITAR'),
    ('Eliminar usuario', 'USUARIOS', 'ELIMINAR'),

    -- Configuración
    ('Modificar parámetros globales', 'CONFIGURACION', 'EDITAR');

-- ---------- ASOCIACIÓN DE ROLES Y PERMISOS ----------

-- 1. ADMIN tiene todos los permisos del sistema
INSERT INTO rol_permiso (id_rol, id_permiso)
SELECT (SELECT id_rol FROM rol WHERE nombre = 'ADMIN'), id_permiso FROM permiso;

-- 2. GUARDIA solo tiene permisos de visitas/accesos
INSERT INTO rol_permiso (id_rol, id_permiso)
SELECT (SELECT id_rol FROM rol WHERE nombre = 'GUARDIA'), id_permiso FROM permiso
WHERE modulo IN ('VISITAS');

-- 3. RESIDENTE tiene permisos limitados (leer residentes/unidades, crear tickets, crear reservas, ver comunicados, registrar pagos, votar)
INSERT INTO rol_permiso (id_rol, id_permiso)
SELECT (SELECT id_rol FROM rol WHERE nombre = 'RESIDENTE'), id_permiso FROM permiso
WHERE (modulo = 'RESIDENTES' AND accion = 'LEER')
   OR (modulo = 'UNIDADES' AND accion = 'LEER')
   OR (modulo = 'CUOTAS' AND accion = 'LEER')
   OR (modulo = 'PAGOS' AND (accion = 'LEER' OR accion = 'CREAR'))
   OR (modulo = 'TICKETS' AND (accion = 'LEER' OR accion = 'CREAR'))
   OR (modulo = 'RESERVAS' AND (accion = 'LEER' OR accion = 'CREAR'))
   OR (modulo = 'COMUNICADOS' AND accion = 'LEER')
   OR (modulo = 'ASAMBLEAS' AND (accion = 'LEER' OR accion = 'EDITAR'));

-- 4. TESORERO tiene permisos financieros y de lectura general
INSERT INTO rol_permiso (id_rol, id_permiso)
SELECT (SELECT id_rol FROM rol WHERE nombre = 'TESORERO'), id_permiso FROM permiso
WHERE modulo IN ('CUOTAS', 'PAGOS', 'REPORTES')
   OR (modulo IN ('RESIDENTES', 'UNIDADES') AND accion = 'LEER');
