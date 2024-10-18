USE castores;

-- Crear el usuario ADMIN si no existe
IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = 'admin_user')
BEGIN
    CREATE USER admin_user WITH PASSWORD = '1234';
END

-- Asignar permisos al usuario ADMIN
GRANT SELECT, INSERT, UPDATE, DELETE ON productos TO admin_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON estado_producto TO admin_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ventas TO admin_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON historico_movimiento TO admin_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON usuarios TO admin_user;

-- También puedes asignar permisos de administración a nivel de base de datos
ALTER ROLE db_owner ADD MEMBER admin_user;


-- Crear el usuario ALMACENISTA si no existe
IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = 'almacenista_user')
BEGIN
    CREATE USER almacenista_user WITH PASSWORD = '1234';
END

-- Asignar permisos al usuario ALMACENISTA
GRANT SELECT ON productos TO almacenista_user;
GRANT SELECT ON estado_producto TO almacenista_user;
GRANT SELECT ON historico_movimiento TO almacenista_user;
GRANT SELECT ON ventas TO almacenista_user;

-- Permitir registrar entradas y salidas
GRANT INSERT ON ventas TO almacenista_user;


/*
USE castores;

-- Eliminar el usuario ADMIN si existe
IF EXISTS (SELECT * FROM sys.database_principals WHERE name = 'admin_user')
BEGIN
    DROP USER admin_user;
END

-- Eliminar el usuario ALMACENISTA si existe
IF EXISTS (SELECT * FROM sys.database_principals WHERE name = 'almacenista_user')
BEGIN
    DROP USER almacenista_user;
END

*/