package com.mrozm.schoollibrary.process.book

import com.mrozm.schoollibrary.core.IMapper
import com.mrozm.schoollibrary.process.book.model.dto.Book
import com.mrozm.schoollibrary.process.book.model.entity.BookEntity
import org.springframework.stereotype.Component

@Component
class BookMapper : IMapper<Book, BookEntity> {

    override fun mapTo(a: Book): BookEntity = BookEntity(
        isbn = a.isbn,
        title = a.title,
        author = a.author,
        category = enumValueOf(a.category.name),
        format = enumValueOf(a.format.name),
        release = a.release
    )

    override fun mapFrom(b: BookEntity): Book = Book(
        isbn = b.isbn,
        title = b.title,
        author = b.author,
        category = enumValueOf(b.category.name),
        format = enumValueOf(b.format.name),
        release = b.release
    )

}
