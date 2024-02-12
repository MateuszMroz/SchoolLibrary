package com.mrozm.schoollibrary.process.book

import com.mrozm.schoollibrary.core.IMapper
import com.mrozm.schoollibrary.process.book.model.dto.BookResponse
import com.mrozm.schoollibrary.process.book.model.entity.BookEntity
import org.springframework.stereotype.Component

@Component
class BookMapper : IMapper<BookResponse, BookEntity> {

    override fun mapTo(a: BookResponse): BookEntity = BookEntity(
        isbn = a.isbn,
        title = a.title,
        author = a.author,
        category = enumValueOf(a.category.name),
        format = enumValueOf(a.format.name),
        release = a.release
    )

    override fun mapFrom(b: BookEntity): BookResponse = BookResponse(
        isbn = b.isbn,
        title = b.title,
        author = b.author,
        category = enumValueOf(b.category.name),
        format = enumValueOf(b.format.name),
        release = b.release
    )

}
