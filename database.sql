CREATE TYPE class AS ENUM('B1', 'B2', 'B3', 'M1', 'M2');

CREATE TABLE IF NOT EXISTS user (
    usr_id INT NOT NULL AUTO_INCREMENT,
    usr_mail VARCHAR (50) NOT NULL,
    usr_first_name VARCHAR (50) NOT NULL,
    usr_last_name VARCHAR (50) NOT NULL,
    usr_password VARCHAR (100) NOT NULL,
    PRIMARY KEY (usr_id)
);

CREATE TABLE IF NOT EXISTS student(
    stud_id INT NOT NULL AUTO_INCREMENT,
    stud_first_name VARCHAR (50) NOT NULL,
    stud_last_name VARCHAR (50) NOT NULL,
    stud_date_birth DATE NOT NULL,
    stud_average_grade FLOAT,
    stud_class class,
    PRIMARY KEY (stud_id)
);
