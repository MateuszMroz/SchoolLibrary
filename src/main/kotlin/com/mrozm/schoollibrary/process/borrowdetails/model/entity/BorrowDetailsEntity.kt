package com.mrozm.schoollibrary.process.borrowdetails.model.entity

import com.mrozm.schoollibrary.process.book.model.entity.CategoryEntity
import com.mrozm.schoollibrary.process.book.model.entity.FormatEntity
import com.mrozm.schoollibrary.process.borrowdetails.model.entity.StatusEntity.BORROWED
import java.time.LocalDate

data class BorrowDetailsEntity(
    val studentId: Long,
    val bookIsbn: String,
    val borrowDate: LocalDate = LocalDate.now(),
    val returnDate: LocalDate? = null,
    val status: StatusEntity = BORROWED
)

data class BorrowDetailsFullEntity(
    val studentId: Long,
    val studentFirstname: String,
    val studentLastname: String,
    val bookIsbn: String,
    val bookTitle: String,
    val bookAuthor: String,
    val bookCategory: CategoryEntity,
    val bookFormat: FormatEntity,
    val bookRelease: LocalDate,
    val borrowDate: LocalDate = LocalDate.now(),
    val returnDate: LocalDate? = null,
    val borrowStatus: StatusEntity = BORROWED
)

enum class StatusEntity {
    BORROWED, RETURNED
}