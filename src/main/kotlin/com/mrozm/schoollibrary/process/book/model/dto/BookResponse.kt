package com.mrozm.schoollibrary.process.book.model.dto

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE
import java.time.LocalDate

data class BookResponse(
    val isbn: String,
    val title: String,
    val author: String,
    val category: Category,
    val format: Format,
    @DateTimeFormat(iso = DATE) val release: LocalDate
)

enum class Category {
    FANTASY,
    SCIENCE_FICTION,
    DYSTOPIAN,
    ADVENTURE,
    ROMANCE,
    DETECTIVE,
    HORROR,
    THRILLER,
    LGBTQ,
    HISTORICAL_FICTION,
    YOUNG_ADULT,
    BIOGRAPHY,
    COOKING,
    ART,
    MOTIVATIONAL,
    HEALTH,
    HISTORY,
    BUSINESS,
    RELIGION,
    LAW,
    TRAVEL,
    TRUE_CRIME,
    CHILDREN
}

enum class Format {
    HARDCOVER, EBOOK, AUDIO, PAPERBACK
}