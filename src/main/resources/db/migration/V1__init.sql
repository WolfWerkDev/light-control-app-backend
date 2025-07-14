-- V1__init.sql - Creación de tablas limpias con estructura actual

-- Crear tabla usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombres VARCHAR(255),
    apellidos VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    login VARCHAR(255)
);

-- Crear tabla productos
CREATE TABLE productos (
    id_modulo SERIAL PRIMARY KEY,
    id_usuario INT,
    capacidad_modulo INT,
    nombre_modulo VARCHAR(255),
    codigo_validacion VARCHAR(9) UNIQUE NOT NULL,
    CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Crear tabla luces
CREATE TABLE luces (
    id_luz SERIAL PRIMARY KEY,
    id_modulo INT NOT NULL,
    numero_luz INT,
    estado BOOLEAN,
    nombre_luz VARCHAR(255),
    CONSTRAINT fk_modulo FOREIGN KEY (id_modulo) REFERENCES productos(id_modulo) ON DELETE CASCADE
);
