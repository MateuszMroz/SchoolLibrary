package com.mrozm.schoollibrary.process.bookreservation.model.dto

import com.mrozm.schoollibrary.process.book.model.dto.BookResponse
import com.mrozm.schoollibrary.process.bookreservation.model.dto.Status.BORROWED
import java.time.LocalDate

data class BookReservationResponse(
    val student: StudentResponse,
    val book: BookResponse,
    val borrowDate: LocalDate,
    val returnDate: LocalDate? = null,
    val status: Status = BORROWED
)

data class StudentResponse(val uuid: String, val firstname: String, val lastname: String)

enum class Status {
    BORROWED, RETURNED
}