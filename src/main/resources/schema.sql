DROP TABLE IF EXISTS course;

CREATE TABLE course (
 id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
 code varchar(100) NOT NULL,
 name varchar(100) NOT NULL,
 description varchar(100),
 credits int
);
