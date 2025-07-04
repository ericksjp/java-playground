INSERT INTO USERS (id, username, password, role) VALUES (100, 'erick@mail.com', '$2a$12$GsBtFjD29Mu6OL/twhQwx.aSwYSvWkKk9cYfshOKKCRo1cYUFcqA2', 'ROLE_ADMIN');
INSERT INTO USERS (id, username, password, role) VALUES (101, 'maria@mail.com', '$2a$12$GsBtFjD29Mu6OL/twhQwx.aSwYSvWkKk9cYfshOKKCRo1cYUFcqA2', 'ROLE_CLIENT');
INSERT INTO USERS (id, username, password, role) VALUES (102, 'jorge@mail.com', '$2a$12$GsBtFjD29Mu6OL/twhQwx.aSwYSvWkKk9cYfshOKKCRo1cYUFcqA2', 'ROLE_CLIENT');

INSERT INTO CLIENTS (id, name, cpf, user_id, visits) VALUES (101, 'Maria Maria', '85015292066', 101, 1);
INSERT INTO CLIENTS (id, name, cpf, user_id, visits) VALUES (102, 'Jorge Jorge', '23168131008', 102, 10);

INSERT INTO SPOTS (id, code, status) VALUES (101, '0001', 'OCCUPIED');
INSERT INTO SPOTS (id, code, status) VALUES (102, '0002', 'OCCUPIED');
INSERT INTO SPOTS (id, code, status) VALUES (103, '0003', 'OCCUPIED');
INSERT INTO SPOTS (id, code, status) VALUES (104, '0004', 'FREE');
INSERT INTO SPOTS (id, code, status) VALUES (105, '0005', 'FREE');

insert into CLIENT_SPOT (receipt, license_plate, brand, model, color, checkin, client_id, spot_id)
    values ('20230313-101300', 'FIT-1010', 'FIAT', 'PALIO', 'VERDE', '2024-03-13 10:15:00', 102, 101);
insert into CLIENT_SPOT (receipt, license_plate, brand, model, color, checkin, client_id, spot_id)
    values ('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'BRANCO', '2024-03-14 10:15:00', 101, 102);
insert into CLIENT_SPOT (receipt, license_plate, brand, model, color, checkin, client_id, spot_id)
    values ('20230315-101500', 'FIT-1030', 'FIAT', 'PALIO', 'VERDE', '2024-03-14 10:15:00', 102, 103);
