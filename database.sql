CREATE TYPE level AS ENUM('B1', 'B2', 'B3', 'M1', 'M2');

CREATE TABLE IF NOT EXISTS "user" (
    usr_id SERIAL PRIMARY KEY,
    usr_mail VARCHAR (50) NOT NULL,
    usr_password VARCHAR (100) NOT NULL
);

CREATE TABLE IF NOT EXISTS "student"(
    stud_id SERIAL PRIMARY KEY,
    stud_number VARCHAR(8) NOT NULL,
    stud_first_name VARCHAR (50) NOT NULL,
    stud_last_name VARCHAR (50) NOT NULL,
    stud_birth_date DATE NOT NULL,
    stud_level level,    
    stud_average_grade FLOAT
);

INSERT INTO "user" (usr_mail, usr_password) VALUES ('julien@mail.com', 'test');

INSERT INTO "student" (stud_number, stud_first_name, stud_last_name, stud_birth_date, stud_level, stud_average_grade)
VALUES
('00000001', 'Clotaire', 'Dupont', '1996-01-02', 'B3', 12.0),
('00000002', 'Sylvie', 'Dupont', '1990-01-02', 'B3', 15.5),
('00000003', 'Jeanne', 'Martin', '1990-04-02', 'B2', 12.0),
('00000004', 'Bruno', 'Martin', '1996-05-02', 'B1', 15.5),
('00000005', 'Nathalie', 'Lefebvre', '1992-01-02', 'B1', 12.0),
('00000006', 'Jean-Pierre', 'Lefebvre', '1992-08-02', 'B1', 8.8),
('00000007', 'Marc', 'Gaillard', '1996-08-02', 'M1', 12.0),
('00000008', 'Sophie', 'Fournil', '1998-01-02', 'M2', 11.5),
('00000009', 'Laurent', 'Fournil', '1996-01-02', 'M2', 11.5);
