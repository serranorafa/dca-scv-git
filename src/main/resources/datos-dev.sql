/* Populate tables */
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, admin, bloqueado) VALUES('1', 'domingo@ua', 'Domingo Gallardo', '123', '2001-02-10', false, false);
INSERT INTO tareas (id, titulo, usuario_id) VALUES('1', 'Lavar coche', '1');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('2', 'Renovar DNI', '1');
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, admin, bloqueado) VALUES('2', 'js@ua', 'John Smith', '123', '1992-12-10', false, false);
INSERT INTO tareas (id, titulo, usuario_id) VALUES('3', 'Comprar pan', '2');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('4', 'Poner gasolina', '2');
INSERT INTO tareas (id, titulo, usuario_id) VALUES('5', 'Estudiar examen', '2');
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, admin, bloqueado) VALUES('3', 'admin@ua', 'Administrador García', '123', '1995-05-09', true, false);
INSERT INTO tareas (id, titulo, usuario_id) VALUES('6', 'Hacer la práctica de MADS', '3');
INSERT INTO equipos (id, nombre) VALUES('1', 'Proyecto P1');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('1', '1');
INSERT INTO equipo_usuario (fk_equipo, fk_usuario) VALUES('1', '2');
INSERT INTO equipos (id, nombre) VALUES('2', 'Proyecto P0');
INSERT INTO usuarios (id, email, nombre, password, fecha_nacimiento, admin, bloqueado) VALUES('4', 'unusuario@ua', 'Señor Usuario', '123', '1992-12-25', false, false);
INSERT INTO equipos (id, nombre) VALUES('3', 'Equipo número 5');