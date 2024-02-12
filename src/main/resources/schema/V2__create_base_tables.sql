CREATE TABLE student
(
    id        SERIAL PRIMARY KEY,
    firstname VARCHAR(100) NOT NULL,
    lastname  VARCHAR(100) NOT NULL,
    email     VARCHAR(200) NOT NULL UNIQUE,
    pass      VARCHAR(300) NOT NULL,
    role      VARCHAR(30)  NOT NULL
);

CREATE TABLE book
(
    isbn     VARCHAR(100) NOT NULL PRIMARY KEY,
    title    VARCHAR(150) NOT NULL,
    author   VARCHAR(200) NOT NULL,
    category VARCHAR(30)  NOT NULL,
    format   VARCHAR(300) NOT NULL,
    release  DATE         NOT NULL
);

CREATE TABLE borrow
(
    id          SERIAL PRIMARY KEY,
    student_id  INTEGER      NOT NULL,
    book_isbn   VARCHAR(100) NOT NULL,
    borrow_date DATE         NOT NULL,
    return_date DATE,
    status      VARCHAR(20),
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES student (id) ON DELETE CASCADE,
    CONSTRAINT fk_book FOREIGN KEY (book_isbn) REFERENCES book (isbn) ON DELETE CASCADE
)