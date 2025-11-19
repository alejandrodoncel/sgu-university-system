SET NAMES utf8mb4;

-- =================================================================
-- ARCHIVO DE INICIALIZACIÓN ÚNICO PARA LA BASE DE DATOS SGU
-- Contiene la creación de la BD, tablas y datos de prueba.
-- =================================================================

-- Alterar la base de datos para asegurar la codificación correcta
ALTER DATABASE sgu_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Creación de la tabla 'students'
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_date DATE,
    email VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(255)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Creación de la tabla 'professors'
CREATE TABLE IF NOT EXISTS professors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    specialty VARCHAR(100) NOT NULL
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Creación de la tabla 'subjects'
CREATE TABLE IF NOT EXISTS subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    credits INT NOT NULL,
    professor_id INT,
    FOREIGN KEY (professor_id) REFERENCES professors(id) ON DELETE SET NULL
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Creación de la tabla 'enrollments'
CREATE TABLE IF NOT EXISTS enrollments (
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    enrollment_date DATE NOT NULL,
    grade DECIMAL(4, 2), -- Columna para la nota
    PRIMARY KEY (student_id, subject_id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =================================================================
-- INSERCIÓN DE DATOS DE PRUEBA
-- =================================================================

-- Insertar Profesores
INSERT INTO professors (id, dni, first_name, last_name, specialty) VALUES
(1, 'P-98765432', 'Hernán', 'Cortés', 'Ingeniería de Software'),
(2, 'P-12312312', 'Elba', 'Lazo', 'Bases de Datos'),
(3, 'P-45645645', 'Ángel', 'Bermúdez', 'Inteligencia Artificial'),
(4, 'P-78978978', 'Beatriz', 'Pinzón', 'Ciberseguridad');

-- Insertar Estudiantes
INSERT INTO students (id, dni, first_name, last_name, birth_date, email, address) VALUES
(1, '11111111A', 'Julián', 'Muñoz', '2002-05-10', 'julian.munoz@example.com', 'Calle del Ángel Caído 12'),
(2, '22222222B', 'Sofía', 'Núñez', '2003-01-15', 'sofia.nunez@example.com', 'Avenida de la Ilustración 74'),
(3, '33333333C', 'Sebastián', 'Yáñez', '2002-11-30', 'sebastian.yanez@example.com', 'Plaza de España 1'),
(4, '44444444D', 'Lucía', 'Martínez', '2003-07-22', 'lucia.martinez@example.com', 'Paseo de la Castellana 100'),
(5, '55555555E', 'Óscar', 'Giménez', '2001-03-12', 'oscar.gimenez@example.com', 'Calle de Alcalá 210');

-- Insertar Asignaturas
INSERT INTO subjects (id, name, credits, professor_id) VALUES
(1, 'Cálculo Numérico', 6, 1),
(2, 'Sistemas de Bases de Datos', 6, 2),
(3, 'Introducción a la IA', 6, 3),
(4, 'Programación Concurrente y Distribuida', 5, 1),
(5, 'Diseño de Interfaces Gráficas', 4, null), -- Asignatura vacante
(6, 'Hacking Ético', 5, 4);

-- Insertar Matrículas y Notas
-- Estudiante 1 (Julián Muñoz)
INSERT INTO enrollments (student_id, subject_id, enrollment_date, grade) VALUES
(1, 1, '2025-09-01', 8.50),
(1, 2, '2025-09-01', 7.75),
(1, 4, '2025-09-01', 9.10);

-- Estudiante 2 (Sofía Núñez)
INSERT INTO enrollments (student_id, subject_id, enrollment_date, grade) VALUES
(2, 2, '2025-09-01', 9.50),
(2, 3, '2025-09-01', 8.00),
(2, 6, '2025-09-01', 10.00);

-- Estudiante 3 (Sebastián Yáñez)
INSERT INTO enrollments (student_id, subject_id, enrollment_date, grade) VALUES
(3, 4, '2025-09-01', 6.50),
(3, 5, '2025-09-01', null); -- Sin calificar

-- Estudiante 4 (Lucía Martínez)
INSERT INTO enrollments (student_id, subject_id, enrollment_date, grade) VALUES
(4, 1, '2025-09-02', 7.00),
(4, 2, '2025-09-02', 6.25),
(4, 3, '2025-09-02', 8.80),
(4, 5, '2025-09-02', 9.90);

-- Estudiante 5 (Óscar Giménez)
INSERT INTO enrollments (student_id, subject_id, enrollment_date, grade) VALUES
(5, 1, '2025-09-03', 4.50),
(5, 6, '2025-09-03', 5.00);