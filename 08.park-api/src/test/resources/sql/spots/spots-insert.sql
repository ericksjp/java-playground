INSERT INTO USERS (id, username, password, role) VALUES (100, 'erick@mail.com', '$2a$12$GsBtFjD29Mu6OL/twhQwx.aSwYSvWkKk9cYfshOKKCRo1cYUFcqA2', 'ROLE_ADMIN');
INSERT INTO USERS (id, username, password, role) VALUES (101, 'maria@mail.com', '$2a$12$GsBtFjD29Mu6OL/twhQwx.aSwYSvWkKk9cYfshOKKCRo1cYUFcqA2', 'ROLE_CLIENT');
INSERT INTO USERS (id, username, password, role) VALUES (102, 'jorge@mail.com', '$2a$12$GsBtFjD29Mu6OL/twhQwx.aSwYSvWkKk9cYfshOKKCRo1cYUFcqA2', 'ROLE_CLIENT');
INSERT INTO USERS (id, username, password, role) VALUES (103, 'bob@mail.com', '$2a$12$GsBtFjD29Mu6OL/twhQwx.aSwYSvWkKk9cYfshOKKCRo1cYUFcqA2', 'ROLE_CLIENT');

INSERT INTO CLIENTS (id, name, cpf, user_id, visits) VALUES (101, 'Maria Maria', '85015292066', 101, 0);
INSERT INTO CLIENTS (id, name, cpf, user_id, visits) VALUES (102, 'Jorge Jorge', '23168131008', 102, 0);

INSERT INTO SPOTS (id, code, status) VALUES (101, '0001', 'FREE');
INSERT INTO SPOTS (id, code, status) VALUES (102, '0002', 'OCCUPIED');
