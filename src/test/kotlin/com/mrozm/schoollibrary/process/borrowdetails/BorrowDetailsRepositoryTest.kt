package com.mrozm.schoollibrary.process.borrowdetails

import com.mrozm.schoollibrary.process.book.model.entity.CategoryEntity.MOTIVATIONAL
import com.mrozm.schoollibrary.process.book.model.entity.FormatEntity.PAPERBACK
import com.mrozm.schoollibrary.process.borrowdetails.model.entity.BorrowDetailsEntity
import com.mrozm.schoollibrary.process.borrowdetails.model.entity.StatusEntity.BORROWED
import com.mrozm.schoollibrary.process.borrowdetails.model.entity.StatusEntity.RETURNED
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
import java.time.LocalDate

@MybatisTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class BorrowDetailsRepositoryTest {

    @Autowired
    lateinit var repository: BorrowDetailsRepository

    @Test
    fun `should return borrow book details when operations has been occur`() {
        // given
        val studentId = 1L
        val isbn = "000-0-00-000000-1"
        val borrowBook = BorrowDetailsEntity(
            studentId = studentId,
            bookIsbn = isbn,
            borrowDate = LocalDate.of(2023, 10, 10)
        )
        repository.borrowBook(borrowBook)

        // when
        val borrowDetails = repository.findBorrowDetails(studentId, isbn)

        // then
        assertThat(borrowDetails?.studentId).isEqualTo(studentId)
        assertThat(borrowDetails?.bookIsbn).isEqualTo(isbn)
        assertThat(borrowDetails?.borrowDate).isEqualTo(LocalDate.of(2023, 10, 10))
    }

    @Test
    fun `should return borrow book full details when operations has been occur`() {
        // given
        val studentId = 1L
        val isbn = "000-0-00-000000-1"
        val borrowBook = BorrowDetailsEntity(
            studentId = studentId,
            bookIsbn = isbn,
            borrowDate = LocalDate.of(2023, 10, 10)
        )
        repository.borrowBook(borrowBook)

        // when
        val borrowDetails = repository.findFullBorrowDetails(studentId, isbn)

        // then
        assertThat(borrowDetails?.studentId).isEqualTo(studentId)
        assertThat(borrowDetails?.studentFirstname).isEqualTo("Firstname")
        assertThat(borrowDetails?.studentLastname).isEqualTo("Lastname")
        assertThat(borrowDetails?.bookIsbn).isEqualTo(isbn)
        assertThat(borrowDetails?.bookTitle).isEqualTo("title-test")
        assertThat(borrowDetails?.bookAuthor).isEqualTo("author-test")
        assertThat(borrowDetails?.bookCategory).isEqualTo(MOTIVATIONAL)
        assertThat(borrowDetails?.bookFormat).isEqualTo(PAPERBACK)
        assertThat(borrowDetails?.bookRelease).isEqualTo(LocalDate.of(2016, 6, 23))
        assertThat(borrowDetails?.borrowStatus).isEqualTo(BORROWED)
        assertThat(borrowDetails?.borrowDate).isEqualTo(LocalDate.of(2023, 10, 10))
        assertThat(borrowDetails?.returnDate).isNull()

    }

    @Test
    fun `should return affected columns count when save borrow book details`() {
        // given
        val borrowDetails = BorrowDetailsEntity(
            studentId = 1L,
            bookIsbn = "000-0-00-000000-1"
        )

        // when
        val rowAffected = repository.borrowBook(borrowDetails)

        // then
        assertThat(rowAffected).isGreaterThan(0)
    }

    @Test
    fun `should update borrow book details when update return borrow state`() {
        // given
        val studentId = 1L
        val isbn = "000-0-00-000000-1"
        val borrowBook = BorrowDetailsEntity(
            studentId = studentId,
            bookIsbn = isbn,
            borrowDate = LocalDate.of(2023, 10, 10)
        )
        repository.borrowBook(borrowBook)

        // when
        repository.returnBook(studentId, isbn)
        val borrowDetails = repository.findBorrowDetails(studentId, isbn)

        // then
        assertThat(borrowDetails?.returnDate).isNotNull()
        assertThat(borrowDetails?.status).isEqualTo(RETURNED)
    }

}