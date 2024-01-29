package com.mrozm.schoollibrary.process.borrowdetails.model.dto

import com.mrozm.schoollibrary.process.book.model.dto.Book
import com.mrozm.schoollibrary.process.borrowdetails.model.dto.Status.BORROWED
import java.time.LocalDate

data class BorrowDetailsFull(
    val student: Student,
    val book: Book,
    val borrowDate: LocalDate = LocalDate.now(),
    val returnDate: LocalDate? = null,
    val status: Status = BORROWED
)

data class Student(val id: Long, val firstname: String, val lastname: String)

enum class Status {
    BORROWED, RETURNED
}