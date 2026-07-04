-- ============================================================
-- V9__fix_admin_password.sql
-- Corrige el hash del usuario admin de desarrollo.
-- Contraseña: Admin@123
-- Hash generado con BCrypt factor 12.
-- ============================================================
UPDATE usuario
SET password_hash = '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE username = 'admin';
