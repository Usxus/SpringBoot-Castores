/*
DROP DATABASE Castores
*/

CREATE DATABASE Castores;

-- Usar base de datos
USE Castores;

-- Crear la tabla de productos
CREATE TABLE productos (
    idProducto INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(40) NOT NULL,
    precio DECIMAL(16,2) NOT NULL,
    cantidad INT NOT NULL
);

-- Insertar datos de prueba en productos
INSERT INTO productos (nombre, precio, cantidad) VALUES 
('LAPTOP', 3000.00, 10),
('PC', 4000.00, 5),
('MOUSE', 100.00, 50),
('TECLADO', 150.00, 20),
('MONITOR', 2000.00, 15),
('MICROFONO', 350.00, 30),
('AUDIFONOS', 450.00, 25);

-- Crear la tabla de estado del producto con idEstadoProducto como PRIMARY KEY
CREATE TABLE estado_producto (
    idEstadoProducto INT IDENTITY(1,1) PRIMARY KEY,
    idProducto INT,
    activo BIT NOT NULL DEFAULT 1,
    FOREIGN KEY (idProducto) REFERENCES productos(idProducto)
);

-- Insertar datos de prueba en estado_producto
INSERT INTO estado_producto (idProducto, activo) VALUES 
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 1);

-- Crear la tabla de ventas para manejar las cantidades
CREATE TABLE ventas (
    idVenta INT IDENTITY(1,1) PRIMARY KEY,
    idProducto INT,
    cantidad INT NOT NULL,
    FOREIGN KEY (idProducto) REFERENCES productos(idProducto)
);

-- Insertar registros de ventas
INSERT INTO ventas (idProducto, cantidad) VALUES 
(1, 10),
(2, 5),
(3, 7),
(4, 3),
(5, 2),
(6, 8),
(7, 6);

-- Crear la tabla de movimientos históricos
CREATE TABLE historico_movimiento (
    id_movimiento INT IDENTITY(1,1) PRIMARY KEY,
    id_producto INT,
    cantidad INT NOT NULL,
    tipo_movimiento VARCHAR(50),
    fecha_movimiento DATETIME,
    usuario VARCHAR(100),
    FOREIGN KEY (id_producto) REFERENCES productos(idProducto)
);

-- Crear la tabla de usuarios
CREATE TABLE usuarios (
    idUsuario INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100),
    correo VARCHAR(50),
    contraseña VARCHAR(100),
    idRol INT, 
    estatus INT
);

-- Insertar datos de prueba en usuarios
INSERT INTO usuarios (nombre, correo, contraseña, idRol, estatus) VALUES 
('admin_user', 'admin@example.com', '1234', 1, 1),
('almacenista_user', 'almacenista@example.com', '1234', 2, 1);


-- Consultas
select * from productos;
select * from ventas;
select * from estado_producto;
select * from historico_movimiento;
select * from usuarios;

/*
drop table ventas;
drop table estado_producto;
drop table historico_movimiento;
drop table usuarios;
drop table productos;
*/