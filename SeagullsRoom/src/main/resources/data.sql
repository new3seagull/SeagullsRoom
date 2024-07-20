INSERT INTO user_tb (email, name, password, role)
VALUES ('abc1@example.com', 'name1', 'password123', 'ROLE_USER');

INSERT INTO user_tb (email, name, password, role)
VALUES ('abc2@example.com', 'name2', 'password456', 'ROLE_USER');

INSERT INTO user_tb (email, name, password, role)
VALUES ('test@naver.com', 'lee', 'test1234', 'ROLE_USER');

INSERT INTO study (user_id, study_time, study_date, updated_at)
VALUES (1, '02:30:00', now(), now()),
       (2, '03:15:00', now(), now()),
       (2, '04:00:00', now(), now());