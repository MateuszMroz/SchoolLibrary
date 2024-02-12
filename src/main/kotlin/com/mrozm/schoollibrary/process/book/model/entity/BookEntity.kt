package com.mrozm.schoollibrary.process.book.model.entity

import com.mrozm.schoollibrary.core.util.NoArg
import java.time.LocalDate

@NoArg
data class BookEntity(
    val isbn: String,
    val title: String,
    val author: String,
    val category: Category,
    val format: Format,
    val release: LocalDate
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
