CREATE TABLE IF NOT EXISTS student
(
    id        SERIAL,
    firstname VARCHAR(100) NOT NULL,
    lastname  VARCHAR(100) NOT NULL,
    email     VARCHAR(200) NOT NULL UNIQUE,
    pass      VARCHAR(300) NOT NULL,
    role      VARCHAR(30)  NOT NULL
);