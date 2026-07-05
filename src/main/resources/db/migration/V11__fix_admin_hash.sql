-- ============================================================
-- V11__fix_admin_hash.sql
-- Restaura un hash válido de BCrypt para el administrador.
-- El script V9 corrompió el hash al modificar manualmente el
-- factor de costo de $10$ a $12$ sin recalcular la sal.
-- Contraseña: password
-- ============================================================
UPDATE usuario
SET password_hash = '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi'
WHERE username = 'admin';
