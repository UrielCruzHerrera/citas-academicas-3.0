-- Script de reseteo de disponibilidad para pruebas de P04
-- Uso: psql -U postgres -d citas_academicas -f sql/03_reset_disponibilidad.sql

-- Limpieza de datos transaccionales
DELETE FROM cambio_estado;
DELETE FROM cita;
DELETE FROM disponibilidad;

-- Resetear secuencias para IDs limpios
ALTER SEQUENCE cambio_estado_id_seq RESTART WITH 1;
ALTER SEQUENCE cita_id_seq RESTART WITH 1;
ALTER SEQUENCE disponibilidad_id_seq RESTART WITH 1;

-- Insertar 15 bloques en los próximos 5 días (3 por día)
INSERT INTO disponibilidad (id_asesor, fecha, hora_inicio, hora_fin, estado) VALUES
(2, CURRENT_DATE + INTERVAL '1 day', '09:00', '09:30', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '1 day', '09:30', '10:00', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '1 day', '10:00', '10:30', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '2 day', '09:00', '09:30', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '2 day', '09:30', '10:00', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '2 day', '10:00', '10:30', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '3 day', '11:00', '11:30', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '3 day', '11:30', '12:00', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '3 day', '12:00', '12:30', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '4 day', '14:00', '14:30', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '4 day', '14:30', '15:00', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '4 day', '15:00', '15:30', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '5 day', '16:00', '16:30', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '5 day', '16:30', '17:00', 'DISPONIBLE'),
(2, CURRENT_DATE + INTERVAL '5 day', '17:00', '17:30', 'DISPONIBLE');

-- Resumen
SELECT 'Disponibilidades insertadas: ' || COUNT(*) AS resultado FROM disponibilidad;