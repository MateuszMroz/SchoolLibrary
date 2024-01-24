package com.mrozm.schoollibrary.process.book.model.entity

import java.time.LocalDate

data class BookEntity(
    val isbn: String,
    val title: String,
    val author: String,
    val category: CategoryEntity,
    val format: FormatEntity,
    val release: LocalDate
)

enum class CategoryEntity {
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

enum class FormatEntity {
    HARDCOVER, EBOOK, AUDIO, PAPERBACK
}
