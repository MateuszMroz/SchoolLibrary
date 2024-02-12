package com.mrozm.schoollibrary.process.bookreservation.model.entity

import com.mrozm.schoollibrary.core.util.NoArg
import com.mrozm.schoollibrary.process.book.model.entity.BookEntity
import com.mrozm.schoollibrary.process.bookreservation.model.entity.Status.BORROWED
import java.time.LocalDate

@NoArg
data class BookReservationEntity(
    val studentUuid: String,
    val bookIsbn: String,
    val borrowDate: LocalDate = LocalDate.now(),
    val returnDate: LocalDate? = null,
    val borrowStatus: Status = BORROWED
)

@NoArg
data class BookReservationDetailsEntity(
    val student: StudentEntity,
    val book: BookEntity,
    val borrowDate: LocalDate,
    val returnDate: LocalDate? = null,
    val borrowStatus: Status = BORROWED
)

@NoArg
data class StudentEntity(val uuid: String, val firstname: String, val lastname: String)

enum class Status {
    BORROWED, RETURNED
}