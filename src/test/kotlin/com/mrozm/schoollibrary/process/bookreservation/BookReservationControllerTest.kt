package com.mrozm.schoollibrary.process.bookreservation

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrozm.schoollibrary.process.book.model.dto.BookResponse
import com.mrozm.schoollibrary.process.book.model.dto.Category.MOTIVATIONAL
import com.mrozm.schoollibrary.process.book.model.dto.Format.PAPERBACK
import com.mrozm.schoollibrary.process.bookreservation.model.dto.BookReservationResponse
import com.mrozm.schoollibrary.process.bookreservation.model.dto.Status.BORROWED
import com.mrozm.schoollibrary.process.bookreservation.model.dto.StudentResponse
import com.mrozm.schoollibrary.utils.BOOK_1
import com.mrozm.schoollibrary.utils.TestDataGenerator
import com.mrozm.schoollibrary.utils.TestDataGenerator.Companion.BOOK_ISBN
import com.mrozm.schoollibrary.utils.TestDataGenerator.Companion.STUDENT_UUID
import com.mrozm.schoollibrary.utils.createObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc(addFilters = false) // is it the best way to skip authentication ?
@Transactional
class BookReservationControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var generator: TestDataGenerator

    private val objectMapper: ObjectMapper = createObjectMapper()

    @Nested
    @DisplayName("BorrowBook")
    @TestInstance(PER_METHOD)
    inner class BorrowBook {

        @Test
        @WithMockUser(username = "anna.cleveland@test.pl")
        fun `should return OK status when borrow a book process has been occur`() {
            // given
            generator.addStudent()
            generator.addBook(BOOK_1)

            // when
            val performPost = mockMvc.post("$BASE_URL/reserve?isbn=$BOOK_ISBN") {
                contentType = APPLICATION_JSON
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isOk() } }
        }

        @Test
        @WithMockUser(username = "anna.cleveland@test.pl")
        fun `should return NOT_FOUND status when try to borrow a book and books does not exist in db`() {
            // given
            generator.addStudent()

            // when
            val performPost = mockMvc.post("$BASE_URL/reserve?isbn=$BOOK_ISBN") {
                contentType = APPLICATION_JSON
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }

        @Test
        @WithMockUser(username = "test.test@test.pl")
        fun `should return NOT_FOUND status when try to borrow a book and user does not exist in db`() {
            // given
            generator.addStudent()
            generator.addBook(BOOK_1)

            // when
            val performPost = mockMvc.post("$BASE_URL/reserve?isbn=$BOOK_ISBN") {
                contentType = APPLICATION_JSON
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }

    }

    @Nested
    @DisplayName("ReturnBook")
    @TestInstance(PER_METHOD)
    inner class ReturnBook {

        @Test
        @WithMockUser(username = "anna.cleveland@test.pl")
        fun `should return OK status when return a book`() {
            // given
            generator.addStudent()
            generator.addBook(BOOK_1)
            generator.addReservation()

            // when
            val performPost = mockMvc.post("$BASE_URL/return?isbn=$BOOK_ISBN") {
                contentType = APPLICATION_JSON
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isOk() } }
        }

        @Test
        @WithMockUser(username = "anna.cleveland@test.pl")
        fun `should return NOT_FOUND status when book does not borrowed`() {
            // given
            generator.addStudent()
            generator.addBook(BOOK_1)

            // when
            val performPost = mockMvc.post("$BASE_URL/return?isbn=$BOOK_ISBN") {
                contentType = APPLICATION_JSON
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("BorrowDetails")
    @TestInstance(PER_METHOD)
    inner class BorrowDetails {

        @Test
        @WithMockUser(username = "anna.cleveland@test.pl")
        fun `should return OK status when return book borrow details`() {
            // given
            generator.addStudent()
            generator.addBook(BOOK_1)
            generator.addReservation()

            // when
            val performPost = mockMvc.get("$BASE_URL?isbn=$BOOK_ISBN") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(
                    BookReservationResponse(
                        student = StudentResponse(uuid = STUDENT_UUID, firstname = "Anne", lastname = "Cleveland"),
                        book = BookResponse(
                            isbn = BOOK_ISBN,
                            title = "The Power of Positive Thinking",
                            author = "Norman Vincent Peale",
                            category = MOTIVATIONAL,
                            format = PAPERBACK,
                            release = LocalDate.of(2023, 10, 10)
                        ),
                        borrowDate = LocalDate.of(2024, 2, 12),
                        returnDate = null,
                        status = BORROWED
                    )
                )
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.student.uuid") { value(STUDENT_UUID) }
                    jsonPath("$.student.firstname") { value("Anne") }
                    jsonPath("$.student.lastname") { value("Cleveland") }
                    jsonPath("$.book.isbn") { value(BOOK_ISBN) }
                    jsonPath("$.book.title") { value("The Power of Positive Thinking") }
                    jsonPath("$.book.author") { value("Norman Vincent Peale") }
                    jsonPath("$.book.category") { value(MOTIVATIONAL.name) }
                    jsonPath("$.book.format") { value(PAPERBACK.name) }
                    jsonPath("$.book.release") { value("2023-10-10") }
                    jsonPath("$.borrowDate") { value("2024-02-12") }
                    jsonPath("$.returnDate") { value(null) }
                    jsonPath("$.status") { value(BORROWED.name) }
                }
        }

        @Test
        @WithMockUser(username = "anna.cleveland@test.pl")
        fun `should return NOT_FOUND status when borrow details does not exist`() {
            // given
            generator.addStudent()
            generator.addBook(BOOK_1)

            // when
            val performPost = mockMvc.get("$BASE_URL?isbn=$BOOK_ISBN") {
                contentType = APPLICATION_JSON
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }

    }

    private companion object {
        const val BASE_URL = "/api/v1/details"
    }
}