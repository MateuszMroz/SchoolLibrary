package com.mrozm.schoollibrary.process.book

import com.mrozm.schoollibrary.process.book.model.dto.Book
import org.springframework.stereotype.Service

interface IBookService {
    fun save(book: Book): Int
    fun findByIsbn(isbn: String): Book
    fun findAll(): List<Book>
    fun update(book: Book)
    fun delete(isbn: String)
}

@Service
class BookService(
    private val repository: BookRepository,
    private val mapper: BookMapper
) : IBookService {

    override fun save(book: Book): Int {
        return repository.save(mapper.mapTo(book))
    }

    override fun findByIsbn(isbn: String): Book {
        return repository.findByIsbn(isbn)?.let { mapper.mapFrom(it) }
            ?: throw NoSuchElementException("Book with isbn: $isbn not found")
    }

    override fun findAll(): List<Book> {
        return repository.findAll().map { mapper.mapFrom(it) }
    }

    override fun update(book: Book) {
        repository.update(book.isbn, mapper.mapTo(book))
    }

    override fun delete(isbn: String) {
        repository.delete(isbn)
    }

}
