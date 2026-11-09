-- base de datos del parcial: consultas medicas
-- se puede correr en MySQL Workbench (File > Open SQL Script > rayo de ejecutar)
-- ojo: la app tambien crea todo sola al arrancar, este script es por si se quiere hacer a mano

CREATE DATABASE IF NOT EXISTS parcial2deprogramacion;
USE parcial2deprogramacion;

-- usuarios para iniciar sesion (rol: ADMIN, MEDICO o PACIENTE)
CREATE TABLE IF NOT EXISTS usuario (
  id bigint NOT NULL AUTO_INCREMENT,
  password varchar(255) DEFAULT NULL,
  rol varchar(255) DEFAULT NULL,
  username varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_usuario_username (username)
);

-- medicos (es la lista desplegable del formulario de consultas)
CREATE TABLE IF NOT EXISTS medico (
  id bigint NOT NULL AUTO_INCREMENT,
  nombre varchar(255) NOT NULL,
  usuario varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_medico_usuario (usuario)
);

-- consultas que registra el admin
CREATE TABLE IF NOT EXISTS consulta (
  id bigint NOT NULL AUTO_INCREMENT,
  consultorio int NOT NULL,
  hora_fin varchar(255) NOT NULL,
  hora_inicio varchar(255) NOT NULL,
  motivo varchar(255) NOT NULL,
  paciente varchar(255) NOT NULL,
  medico_id bigint NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_consulta_medico FOREIGN KEY (medico_id) REFERENCES medico (id),
  CONSTRAINT chk_consultorio CHECK (consultorio BETWEEN 1 AND 999)
);

-- tablas de sesion que usa spring session
CREATE TABLE IF NOT EXISTS SPRING_SESSION (
  PRIMARY_ID char(36) NOT NULL,
  SESSION_ID char(36) NOT NULL,
  CREATION_TIME bigint NOT NULL,
  LAST_ACCESS_TIME bigint NOT NULL,
  MAX_INACTIVE_INTERVAL int NOT NULL,
  EXPIRY_TIME bigint NOT NULL,
  PRINCIPAL_NAME varchar(100) DEFAULT NULL,
  PRIMARY KEY (PRIMARY_ID),
  UNIQUE KEY SPRING_SESSION_IX1 (SESSION_ID),
  KEY SPRING_SESSION_IX2 (EXPIRY_TIME),
  KEY SPRING_SESSION_IX3 (PRINCIPAL_NAME)
) ROW_FORMAT=DYNAMIC;

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES (
  SESSION_PRIMARY_ID char(36) NOT NULL,
  ATTRIBUTE_NAME varchar(200) NOT NULL,
  ATTRIBUTE_BYTES blob NOT NULL,
  PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
  CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
) ROW_FORMAT=DYNAMIC;

-- usuarios de prueba, las claves van en bcrypt
-- admin/admin123, medico/medico123, paciente/paciente123
INSERT IGNORE INTO usuario (username, password, rol) VALUES
('admin', '$2a$10$3XGo8r7JpVRcQ6Y8eH8pCO4ZC9vi5Q3qPiuNwfJMwoAhX1u39kemK', 'ADMIN'),
('medico', '$2a$10$HZb2zWWLKM9lQc7/NjKcAeUDqOOqdyKhA4wGa37sZb2zDm5Hs8UTu', 'MEDICO'),
('paciente', '$2a$10$T4mm9ql6VDMo0Uy6getZG.gRGbEcBtuV39jKbmOBHgCfOU67nDH9W', 'PACIENTE');

-- consultas para ver lo que hay
SELECT * FROM usuario;
SELECT * FROM medico;
SELECT * FROM consulta;

-- consultas con el nombre del medico
SELECT c.id, c.paciente, c.motivo, c.consultorio, c.hora_inicio, c.hora_fin, m.nombre AS medico
FROM consulta c JOIN medico m ON m.id = c.medico_id;

-- sesiones abiertas
SELECT SESSION_ID, PRINCIPAL_NAME FROM SPRING_SESSION;
