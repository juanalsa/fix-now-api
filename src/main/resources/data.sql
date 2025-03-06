INSERT INTO users (id, name, last_name, user_name, password, created_at, updated_at) VALUES ('7ad89fc0-1028-4d15-93af-95ac376538f8', 'Carlos', 'Perez', 'cperez', '12345678', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO users (id, name, last_name, user_name, password, created_at, updated_at) VALUES ('d91adf38-2664-4614-b8f4-cf148d39bdef', 'Juan', 'Ramirez', 'jramirez', '87654321', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tickets (id, description, status, user_id, created_at, updated_at)
VALUES ('1e922874-9e7e-4cdd-aa58-5af5d0f04962', 'El sistema no responde', 'OPEN', '7ad89fc0-1028-4d15-93af-95ac376538f8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tickets (id, description, status, user_id, created_at, updated_at)
VALUES ('8508affc-d8de-4ef2-91e1-b0b4352a75d7', 'El sistema se ha reiniciado 2 veces', 'OPEN', '7ad89fc0-1028-4d15-93af-95ac376538f8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tickets (id, description, status, user_id, created_at, updated_at)
VALUES ('e2ee2017-18d3-4157-a411-d4365ca3b0b1', 'Necesito información de los tickets del mes pasado. [Entregada]', 'CLOSED', 'd91adf38-2664-4614-b8f4-cf148d39bdef', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tickets (id, description, status, user_id, created_at, updated_at)
VALUES ('685836e9-025d-4111-b4a2-b2ea20404627', 'Se me venció la contraseña de mi usuario. [Resuelto]', 'CLOSED', '7ad89fc0-1028-4d15-93af-95ac376538f8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);