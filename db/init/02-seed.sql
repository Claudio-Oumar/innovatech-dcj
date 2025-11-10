-- Script de datos iniciales (seeders)
-- Ejecutado después de 01-schema.sql

-- Nota: Las contraseñas están hasheadas con BCrypt (password123)
-- Hash BCrypt para "password123": $2a$10$vKpJ0Y5gXQXQX5xqK5Qg3eMZQYyN5yJ5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y5Y

-- Esperar a que las tablas sean creadas por Hibernate
-- Usamos una función que espera la existencia de la tabla usuarios

DO $$
BEGIN
    -- Esperar a que la tabla usuarios exista (Hibernate la crea)
    WHILE NOT EXISTS (
        SELECT FROM pg_tables 
        WHERE schemaname = 'public' 
        AND tablename = 'usuarios'
    ) LOOP
        PERFORM pg_sleep(1);
    END LOOP;
    
    RAISE NOTICE 'Tabla usuarios detectada, insertando datos iniciales...';
END $$;

-- Insertar usuarios de prueba (BCrypt hash para 'password123')
INSERT INTO usuarios (username, email, password, nombre_completo, telefono, rol, activo, fecha_creacion)
VALUES 
    ('admin', 'admin@turneropro.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 'Administrador Sistema', '0991234567', 'ADMINISTRADOR', true, NOW()),
    ('barbero1', 'barbero1@turneropro.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 'Carlos Martínez', '0991234568', 'BARBERO', true, NOW()),
    ('barbero2', 'barbero2@turneropro.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 'Miguel Sánchez', '0991234569', 'BARBERO', true, NOW()),
    ('cliente1', 'cliente1@turneropro.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 'Juan Pérez', '0991234570', 'CLIENTE', true, NOW()),
    ('cliente2', 'cliente2@turneropro.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCu', 'María González', '0991234571', 'CLIENTE', true, NOW())
ON CONFLICT (username) DO NOTHING;

-- Esperar a que la tabla servicios exista
DO $$
BEGIN
    WHILE NOT EXISTS (
        SELECT FROM pg_tables 
        WHERE schemaname = 'public' 
        AND tablename = 'servicios'
    ) LOOP
        PERFORM pg_sleep(1);
    END LOOP;
END $$;

-- Insertar servicios de barbería
INSERT INTO servicios (nombre, descripcion, duracion_minutos, precio, activo)
VALUES 
    ('Corte Clásico', 'Corte de cabello estilo clásico con tijeras y máquina', 30, 12.00, true),
    ('Corte + Barba', 'Corte de cabello y arreglo de barba completo', 45, 18.00, true),
    ('Barba', 'Arreglo y perfilado de barba', 20, 8.00, true),
    ('Rapado', 'Rapado completo con máquina', 15, 10.00, true),
    ('Corte Premium', 'Corte premium con lavado y peinado', 60, 25.00, true),
    ('Tinte de Cabello', 'Aplicación de tinte de cabello completo', 90, 35.00, true)
ON CONFLICT DO NOTHING;

-- Esperar a que la tabla horarios_barbero exista
DO $$
BEGIN
    WHILE NOT EXISTS (
        SELECT FROM pg_tables 
        WHERE schemaname = 'public' 
        AND tablename = 'horarios_barbero'
    ) LOOP
        PERFORM pg_sleep(1);
    END LOOP;
END $$;

-- Insertar horarios de ejemplo para barbero1 (lunes a viernes 9:00-18:00)
INSERT INTO horarios_barbero (barbero_id, dia_semana, hora_inicio, hora_fin, activo)
SELECT u.id, dia, '09:00:00', '18:00:00', true
FROM usuarios u, 
     (VALUES ('MONDAY'), ('TUESDAY'), ('WEDNESDAY'), ('THURSDAY'), ('FRIDAY')) AS dias(dia)
WHERE u.username = 'barbero1'
ON CONFLICT DO NOTHING;

-- Insertar horarios de ejemplo para barbero2 (lunes a sábado 10:00-19:00)
INSERT INTO horarios_barbero (barbero_id, dia_semana, hora_inicio, hora_fin, activo)
SELECT u.id, dia, '10:00:00', '19:00:00', true
FROM usuarios u,
     (VALUES ('MONDAY'), ('TUESDAY'), ('WEDNESDAY'), ('THURSDAY'), ('FRIDAY'), ('SATURDAY')) AS dias(dia)
WHERE u.username = 'barbero2'
ON CONFLICT DO NOTHING;

-- Mensaje de confirmación
DO $$ 
BEGIN 
    RAISE NOTICE '===========================================';
    RAISE NOTICE 'Datos iniciales insertados correctamente!';
    RAISE NOTICE '===========================================';
    RAISE NOTICE 'Usuarios de prueba:';
    RAISE NOTICE '  Admin: admin / password123';
    RAISE NOTICE '  Barbero1: barbero1 / password123';
    RAISE NOTICE '  Barbero2: barbero2 / password123';
    RAISE NOTICE '  Cliente1: cliente1 / password123';
    RAISE NOTICE '  Cliente2: cliente2 / password123';
    RAISE NOTICE '===========================================';
    RAISE NOTICE 'Servicios: 6 servicios creados';
    RAISE NOTICE 'Horarios: 2 barberos con horarios configurados';
    RAISE NOTICE '===========================================';
END $$;
