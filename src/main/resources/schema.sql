CREATE TABLE IF NOT EXISTS student
(
    id        SERIAL,
    firstname VARCHAR(100) NOT NULL,
    lastname  VARCHAR(100) NOT NULL,
    email     VARCHAR(200) NOT NULL UNIQUE,
    pass      VARCHAR(300) NOT NULL,
    role      VARCHAR(30)  NOT NULL
);

CREATE TABLE IF NOT EXISTS book
(
    isbn     VARCHAR(100) NOT NULL UNIQUE,
    title    VARCHAR(150) NOT NULL,
    author   VARCHAR(200) NOT NULL,
    category VARCHAR(30)  NOT NULL,
    format   VARCHAR(300) NOT NULL,
    release  DATE         NOT NULL
);