package com.mrozm.schoollibrary.process.book

import com.mrozm.schoollibrary.process.book.model.entity.Category.MOTIVATIONAL
import com.mrozm.schoollibrary.process.book.model.entity.Format.PAPERBACK
import com.mrozm.schoollibrary.utils.BOOK_1
import com.mrozm.schoollibrary.utils.BOOK_2
import com.mrozm.schoollibrary.utils.TestDataGenerator
import com.mrozm.schoollibrary.utils.TestDataGenerator.Companion.BOOK_ISBN
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE
import org.springframework.context.annotation.Import
import java.time.LocalDate

@MybatisTest
@Import(TestDataGenerator::class)
@AutoConfigureTestDatabase(replace = NONE)
class BookRepositoryTest {

    @Autowired
    lateinit var underTest: BookRepository

    @Autowired
    lateinit var generator: TestDataGenerator

    @Test
    fun `should return book when try to find it by isbn`() {
        // given
        generator.addStudent()
        underTest.save(BOOK_1)

        // when
        val book = underTest.findByIsbn(BOOK_ISBN)

        // then
        assertThat(book?.isbn).isEqualTo(BOOK_ISBN)
        assertThat(book?.title).isEqualTo("The Power of Positive Thinking")
        assertThat(book?.author).isEqualTo("Norman Vincent Peale")
        assertThat(book?.category).isEqualTo(MOTIVATIONAL)
        assertThat(book?.format).isEqualTo(PAPERBACK)
        assertThat(book?.release).isEqualTo(LocalDate.of(2023, 10, 10))
    }

    @Test
    fun `should return list of books when try to find it all`() {
        // given
        underTest.save(BOOK_1)
        underTest.save(BOOK_2)

        // when
        val books = underTest.findAll()

        // then
        assertThat(books).isNotEmpty
        assertThat(books.size).isEqualTo(2)
        assertThat(books).containsExactly(BOOK_1, BOOK_2)
    }

    @Test
    fun `should update book when update process has been occur`() {
        // given
        val book = BOOK_1.copy(title = "title-update", author = "author-update")
        underTest.save(book)

        // when
        underTest.update(BOOK_ISBN, book)
        val bookUpdated = underTest.findByIsbn(BOOK_ISBN)

        // then
        assertThat(bookUpdated?.title).isEqualTo("title-update")
        assertThat(bookUpdated?.author).isEqualTo("author-update")
    }

    @Test
    fun `should delete book when delete process has been occur`() {
        // given
        underTest.save(BOOK_1)

        // when
        underTest.delete(BOOK_ISBN)
        val bookDeleted = underTest.findByIsbn(BOOK_ISBN)

        // then
        assertThat(bookDeleted).isNull()
    }
}