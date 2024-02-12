package com.mrozm.schoollibrary.process.book

import com.mrozm.schoollibrary.process.book.model.entity.BookEntity
import org.apache.ibatis.annotations.Mapper

@Mapper
interface BookRepository {

    fun save(bookEntity: BookEntity)
    fun findByIsbn(isbn: String): BookEntity?
    fun findAll(): List<BookEntity>
    fun update(isbn: String, book: BookEntity)
    fun delete(isbn: String)

}
