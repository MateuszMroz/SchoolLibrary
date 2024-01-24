package com.mrozm.schoollibrary.process.book

import com.mrozm.schoollibrary.process.book.model.entity.BookEntity
import com.mrozm.schoollibrary.process.book.model.entity.CategoryEntity.ART
import com.mrozm.schoollibrary.process.book.model.entity.FormatEntity.PAPERBACK
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
import java.time.LocalDate

@MybatisTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class BookRepositoryTest @Autowired constructor(
    private val bookRepository: BookRepository
) {

    private val bookEntity1 = BookEntity(
        isbn = "978-3-16-148410-0",
        title = "title-test",
        author = "author-test",
        category = ART,
        format = PAPERBACK,
        release = LocalDate.of(2024, 1, 24)
    )

    private val bookEntity2 = BookEntity(
        isbn = "978-3-16-148410-1",
        title = "title-test-2",
        author = "author-test-2",
        category = ART,
        format = PAPERBACK,
        release = LocalDate.of(2024, 1, 24)
    )

    @Test
    fun `should return row affected when save book to database`() {
        // given/when
        val rowAffected = bookRepository.save(bookEntity1)

        // then
        assertThat(rowAffected).isGreaterThan(0)
    }

    @Test
    fun `should return book when try to find it by isbn`() {
        // given
        bookRepository.save(bookEntity1)

        // when
        val book = bookRepository.findByIsbn("978-3-16-148410-0")

        // then
        assertThat(book?.isbn).isEqualTo("978-3-16-148410-0")
        assertThat(book?.title).isEqualTo("title-test")
        assertThat(book?.author).isEqualTo("author-test")
        assertThat(book?.category).isEqualTo(ART)
        assertThat(book?.format).isEqualTo(PAPERBACK)
        assertThat(book?.release).isEqualTo(LocalDate.of(2024, 1, 24))
    }

    @Test
    fun `should return list of books when try to find it all`() {
        // given
        bookRepository.save(bookEntity1)
        bookRepository.save(bookEntity2)

        // when
        val books = bookRepository.findAll()

        // then
        assertThat(books).isNotEmpty
        assertThat(books.size).isEqualTo(2)
        assertThat(books).containsExactly(bookEntity1, bookEntity2)
    }

    @Test
    fun `should update book when update process has been occur`() {
        // given
        val isbn = "978-3-16-148410-0"
        val book = bookEntity1.copy(title = "title-update", author = "author-update")
        bookRepository.save(book)

        // when
        bookRepository.update(isbn, book)
        val bookUpdated = bookRepository.findByIsbn(isbn)

        // then
        assertThat(bookUpdated?.title).isEqualTo("title-update")
        assertThat(bookUpdated?.author).isEqualTo("author-update")
    }

    @Test
    fun `should delete book when delete process has been occur`() {
        // given
        val isbn = "978-3-16-148410-0"
        bookRepository.save(bookEntity1)

        // when
        bookRepository.delete(isbn)
        val bookDeleted = bookRepository.findByIsbn(isbn)

        // then
        assertThat(bookDeleted).isNull()
    }
}