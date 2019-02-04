CREATE SEQUENCE seq;

CREATE TABLE Employee (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL
);

CREATE TABLE Progress (
    id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
    employee_id INTEGER NOT NULL,
    progress_date DATE,
    took_in_work INTEGER,
    our_refuges INTEGER,
    all_causes_reserve INTEGER,
    low_reserve INTEGER,
    launched_in_work INTEGER,
    candidate_refuges INTEGER,
    launches_average_term DOUBLE,
    FOREIGN KEY(employee_id) REFERENCES Employee(id)
);