ALTER TABLE borrow
    RENAME TO book_reservation;

ALTER TABLE book_reservation
    DROP CONSTRAINT fk_student;

ALTER TABLE book_reservation
    RENAME COLUMN student_id TO student_uuid;

ALTER TABLE book_reservation
    ALTER COLUMN student_uuid TYPE VARCHAR(36);

ALTER TABLE student
    RENAME COLUMN id TO uuid;

ALTER TABLE student
    ALTER COLUMN uuid TYPE VARCHAR(36);

DROP SEQUENCE student_id_seq CASCADE;

ALTER TABLE book_reservation
    ADD CONSTRAINT fk_student
        FOREIGN KEY (student_uuid)
            REFERENCES student (uuid)
            ON DELETE CASCADE;

ALTER TABLE book_reservation
    DROP COLUMN id;

ALTER TABLE book_reservation
    ADD PRIMARY KEY (student_uuid, book_isbn);
