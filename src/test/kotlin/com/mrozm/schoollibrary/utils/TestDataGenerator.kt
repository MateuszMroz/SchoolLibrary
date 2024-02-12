package com.mrozm.schoollibrary.utils

import com.mrozm.schoollibrary.auth.AuthRepository
import com.mrozm.schoollibrary.auth.model.entity.Role.USER
import com.mrozm.schoollibrary.auth.model.entity.StudentEntity
import com.mrozm.schoollibrary.process.book.BookRepository
import com.mrozm.schoollibrary.process.book.model.entity.BookEntity
import com.mrozm.schoollibrary.process.book.model.entity.Category.ADVENTURE
import com.mrozm.schoollibrary.process.book.model.entity.Category.MOTIVATIONAL
import com.mrozm.schoollibrary.process.book.model.entity.Format.PAPERBACK
import com.mrozm.schoollibrary.process.bookreservation.BookReservationRepository
import com.mrozm.schoollibrary.process.bookreservation.model.entity.BookReservationEntity
import com.mrozm.schoollibrary.utils.TestDataGenerator.Companion.BOOK_ISBN
import com.mrozm.schoollibrary.utils.TestDataGenerator.Companion.BOOK_ISBN_2
import com.mrozm.schoollibrary.utils.TestDataGenerator.Companion.STUDENT_UUID
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class TestDataGenerator(
    private val authRepository: AuthRepository,
    private val bookRepository: BookRepository,
    private val bookReservationRepository: BookReservationRepository
) {
    fun addStudent() {
        authRepository.save(STUDENT)
    }

    fun addBook(vararg books: BookEntity) {
        books.forEach {
            bookRepository.save(it)
        }
    }

    fun addReservation(borrowDate: LocalDate = LocalDate.of(2024, 2, 12)) {
        bookReservationRepository.insert(
            BookReservationEntity(
                studentUuid = STUDENT_UUID,
                bookIsbn = BOOK_ISBN,
                borrowDate = borrowDate
            )
        )
    }

    companion object {
        const val STUDENT_UUID = "uuid-test"
        const val BOOK_ISBN = "000-0-00-000000-1"
        const val BOOK_ISBN_2 = "000-0-00-000000-2"
    }

}

val STUDENT = StudentEntity(
    uuid = STUDENT_UUID,
    firstname = "Anne",
    lastname = "Cleveland",
    email = "anna.cleveland@test.pl",
    pass = "password",
    role = USER,
)

val BOOK_1 = BookEntity(
    isbn = BOOK_ISBN,
    title = "The Power of Positive Thinking",
    author = "Norman Vincent Peale",
    category = MOTIVATIONAL,
    format = PAPERBACK,
    release = LocalDate.of(2023, 10, 10)
)

val BOOK_2 = BookEntity(
    isbn = BOOK_ISBN_2,
    title = "Don Quixote",
    author = "Miguel de Cervantes",
    category = ADVENTURE,
    format = PAPERBACK,
    release = LocalDate.of(2000, 10, 10)
)