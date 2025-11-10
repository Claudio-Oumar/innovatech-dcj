-- Script de inicialización de la base de datos
-- Este script se ejecuta automáticamente cuando se crea el contenedor de PostgreSQL por primera vez

-- Crear extensiones si es necesario
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Las tablas se crean automáticamente por Hibernate (spring.jpa.hibernate.ddl-auto=update)
-- Este archivo es principalmente para documentación o si queremos crear las tablas manualmente

-- Mensaje de confirmación
DO $$ 
BEGIN 
    RAISE NOTICE 'Base de datos inicializada correctamente. Las tablas se crean automáticamente por JPA/Hibernate.';
END $$;
