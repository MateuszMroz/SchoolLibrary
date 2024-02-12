package com.mrozm.schoollibrary.process.bookreservation

import com.mrozm.schoollibrary.core.IMapper
import com.mrozm.schoollibrary.process.book.model.dto.BookResponse
import com.mrozm.schoollibrary.process.book.model.entity.BookEntity
import com.mrozm.schoollibrary.process.bookreservation.model.dto.BookReservationResponse
import com.mrozm.schoollibrary.process.bookreservation.model.dto.StudentResponse
import com.mrozm.schoollibrary.process.bookreservation.model.entity.BookReservationDetailsEntity
import com.mrozm.schoollibrary.process.bookreservation.model.entity.StudentEntity
import org.springframework.stereotype.Component

@Component
class BookReservationMapper : IMapper<BookReservationResponse, BookReservationDetailsEntity> {

    override fun mapTo(a: BookReservationResponse): BookReservationDetailsEntity {
        return BookReservationDetailsEntity(
            student = StudentEntity(
                uuid = a.student.uuid,
                firstname = a.student.firstname,
                lastname = a.student.lastname
            ), book = BookEntity(
                isbn = a.book.isbn,
                title = a.book.title,
                author = a.book.author,
                category = enumValueOf(a.book.category.name),
                format = enumValueOf(a.book.format.name),
                release = a.book.release
            ),
            borrowDate = a.borrowDate,
            returnDate = a.returnDate,
            borrowStatus = enumValueOf(a.status.name)
        )
    }

    override fun mapFrom(b: BookReservationDetailsEntity): BookReservationResponse {
        return BookReservationResponse(
            student = StudentResponse(
                uuid = b.student.uuid,
                firstname = b.student.firstname,
                lastname = b.student.lastname
            ), book = BookResponse(
                isbn = b.book.isbn,
                title = b.book.title,
                author = b.book.author,
                category = enumValueOf(b.book.category.name),
                format = enumValueOf(b.book.format.name),
                release = b.book.release
            ),
            borrowDate = b.borrowDate,
            returnDate = b.returnDate,
            status = enumValueOf(b.borrowStatus.name)
        )
    }

}