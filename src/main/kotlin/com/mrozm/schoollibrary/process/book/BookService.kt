package com.mrozm.schoollibrary.process.book

import com.mrozm.schoollibrary.process.book.model.dto.BookResponse
import org.apache.coyote.BadRequestException
import org.apache.ibatis.javassist.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface IBookService {
    fun save(book: BookResponse)
    fun findByIsbn(isbn: String): BookResponse
    fun findAll(): List<BookResponse>
    fun update(isbn: String, book: BookResponse)
    fun delete(isbn: String)
}

@Service
class BookService(
    private val repository: BookRepository,
    private val mapper: BookMapper
) : IBookService {

    @Transactional
    override fun save(book: BookResponse) {
        if (repository.findByIsbn(book.isbn) != null)
            throw BadRequestException("Book with isbn: ${book.isbn} already exist")

        repository.save(mapper.mapTo(book))
    }

    @Transactional(readOnly = true)
    override fun findByIsbn(isbn: String): BookResponse {
        return repository.findByIsbn(isbn)?.let { mapper.mapFrom(it) }
            ?: throw NotFoundException("Book with isbn: $isbn not found")
    }

    // TODO - find by author

    @Transactional(readOnly = true)
    override fun findAll(): List<BookResponse> {
        return repository.findAll().map { mapper.mapFrom(it) }
    }

    @Transactional
    override fun update(isbn: String, book: BookResponse) {
        if (isbn != book.isbn)
            throw NotFoundException("Isbn in path and body must be the same")

        if (repository.findByIsbn(isbn) == null)
            throw NotFoundException("Book with isbn: $isbn not found")

        repository.update(book.isbn, mapper.mapTo(book))
    }

    @Transactional
    override fun delete(isbn: String) {
        repository.delete(isbn)
    }

}
