package com.mrozm.schoollibrary.process.bookreservation

import com.mrozm.schoollibrary.process.book.model.entity.Category.MOTIVATIONAL
import com.mrozm.schoollibrary.process.book.model.entity.Format.PAPERBACK
import com.mrozm.schoollibrary.process.bookreservation.model.entity.BookReservationEntity
import com.mrozm.schoollibrary.process.bookreservation.model.entity.Status
import com.mrozm.schoollibrary.process.bookreservation.model.entity.Status.RETURNED
import com.mrozm.schoollibrary.utils.BOOK_1
import com.mrozm.schoollibrary.utils.TestDataGenerator
import com.mrozm.schoollibrary.utils.TestDataGenerator.Companion.BOOK_ISBN
import com.mrozm.schoollibrary.utils.TestDataGenerator.Companion.STUDENT_UUID
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
class BookReservationRepositoryTest {

    @Autowired
    lateinit var underTest: BookReservationRepository

    @Autowired
    lateinit var generator: TestDataGenerator

    @Test
    fun `should return book reservation when operations has been occur`() {
        // given
        val bookReservation = BookReservationEntity(
            studentUuid = STUDENT_UUID,
            bookIsbn = BOOK_ISBN,
            borrowDate = LocalDate.of(2024, 2, 10)
        )

        generator.addStudent()
        generator.addBook(BOOK_1)

        underTest.insert(bookReservation)

        // when
        val reservation = underTest.selectBookReservation(STUDENT_UUID, BOOK_ISBN)

        // then
        assertThat(reservation?.studentUuid).isEqualTo(STUDENT_UUID)
        assertThat(reservation?.bookIsbn).isEqualTo(BOOK_ISBN)
        assertThat(reservation?.borrowDate).isEqualTo(LocalDate.of(2024, 2, 10))
    }

    @Test
    fun `should return book reservation details when operations has been occur`() {
        // given
        val bookReservation = BookReservationEntity(
            studentUuid = STUDENT_UUID,
            bookIsbn = BOOK_ISBN,
            borrowDate = LocalDate.of(2024, 2, 10)
        )

        generator.addStudent()
        generator.addBook(BOOK_1)

        underTest.insert(bookReservation)

        // when
        val reservationDetails = underTest.selectDetailsBookReservation(STUDENT_UUID, BOOK_ISBN)

        // then
        assertThat(reservationDetails?.student?.uuid).isEqualTo(STUDENT_UUID)
        assertThat(reservationDetails?.student?.firstname).isEqualTo("Anne")
        assertThat(reservationDetails?.student?.lastname).isEqualTo("Cleveland")
        assertThat(reservationDetails?.book?.isbn).isEqualTo(BOOK_ISBN)
        assertThat(reservationDetails?.book?.title).isEqualTo("The Power of Positive Thinking")
        assertThat(reservationDetails?.book?.author).isEqualTo("Norman Vincent Peale")
        assertThat(reservationDetails?.book?.category).isEqualTo(MOTIVATIONAL)
        assertThat(reservationDetails?.book?.format).isEqualTo(PAPERBACK)
        assertThat(reservationDetails?.book?.release).isEqualTo(LocalDate.of(2023, 10, 10))
        assertThat(reservationDetails?.borrowStatus).isEqualTo(Status.BORROWED)
        assertThat(reservationDetails?.borrowDate).isEqualTo(LocalDate.of(2024, 2, 10))
        assertThat(reservationDetails?.returnDate).isNull()
    }

    @Test
    fun `should update borrow book details when update return borrow state`() {
        // given
        val bookReservation = BookReservationEntity(
            studentUuid = STUDENT_UUID,
            bookIsbn = BOOK_ISBN,
            borrowDate = LocalDate.of(2023, 10, 10)
        )

        generator.addStudent()
        generator.addBook(BOOK_1)

        underTest.insert(bookReservation)

        // when
        underTest.update(STUDENT_UUID, BOOK_ISBN)
        val borrowDetails = underTest.selectBookReservation(STUDENT_UUID, BOOK_ISBN)

        // then
        assertThat(borrowDetails?.returnDate).isNotNull()
        assertThat(borrowDetails?.borrowStatus).isEqualTo(RETURNED)
    }

}