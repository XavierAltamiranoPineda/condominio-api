-- ============================================================
-- V8__test_auth_seed.sql
-- Inserción del usuario administrador de prueba para validación
-- inicial del flujo de autenticación JWT.
-- ============================================================

-- Creamos la Persona base
INSERT INTO persona (tipo_identificacion, numero_identificacion, nombres, apellidos, correo, estado)
VALUES ('CEDULA', '0000000000', 'Super', 'Admin', 'admin@condominio.local', 'ACTIVO');

-- Creamos el Usuario con username 'admin' y password 'Admin@123'
-- (El hash es la versión BCrypt factor 12 exacta de la palabra: Admin@123)
INSERT INTO usuario (id_persona, username, password_hash, estado)
VALUES (
    (SELECT id_persona FROM persona WHERE correo = 'admin@condominio.local'),
    'admin',
    '$2a$12$D23m0tS2Q2x30uL.I8N/B.P.t8SWeT.eZ5Q431IuX8S1mEaE8y4k.',
    'ACTIVO'
);

-- Le asignamos el rol ADMIN
INSERT INTO usuario_rol (id_usuario, id_rol)
VALUES (
    (SELECT id_usuario FROM usuario WHERE username = 'admin'),
    (SELECT id_rol FROM rol WHERE nombre = 'ADMIN')
);
