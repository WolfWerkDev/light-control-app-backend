-- Crear la tabla productos
CREATE TABLE productos (
    id_modulo SERIAL PRIMARY KEY,
    id_usuario INT,
    CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Crear la tabla luces
CREATE TABLE luces (
    id_luz SERIAL PRIMARY KEY,
    id_modulo INT NOT NULL,
    CONSTRAINT fk_modulo FOREIGN KEY (id_modulo) REFERENCES productos(id_modulo) ON DELETE CASCADE,
    UNIQUE (id_modulo, id_luz) -- Garantiza que no haya luces duplicadas en un módulo
);
