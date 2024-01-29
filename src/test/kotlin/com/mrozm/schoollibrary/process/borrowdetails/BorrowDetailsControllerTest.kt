package com.mrozm.schoollibrary.process.borrowdetails

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrozm.schoollibrary.process.book.model.dto.Book
import com.mrozm.schoollibrary.process.book.model.dto.Category
import com.mrozm.schoollibrary.process.book.model.dto.Format
import com.mrozm.schoollibrary.process.borrowdetails.model.dto.BorrowDetailsFull
import com.mrozm.schoollibrary.process.borrowdetails.model.dto.Status
import com.mrozm.schoollibrary.process.borrowdetails.model.dto.Student
import com.mrozm.schoollibrary.utils.createObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD
import org.junit.jupiter.api.extension.ExtendWith
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate


@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc(addFilters = false) // is it the best way to skip authentication ?
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@AutoConfigureMybatis
class BorrowDetailsControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var service: IBorrowDetailsService

    private val objectMapper: ObjectMapper = createObjectMapper()

    @Nested
    @DisplayName("BorrowBook")
    @TestInstance(PER_METHOD)
    inner class BorrowBook {

        @Test
        @WithMockUser(username = "firstname.latsname@test.pl") // is it the best way to mock a SecurityContext?
        fun `should return OK status when borrow a book process has been occur`() {
            // given
            val isbn = "000-0-00-000000-1"

            // when
            val performPost = mockMvc.post("$BASE_URL?isbn=$isbn") {
                contentType = APPLICATION_JSON
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isOk() } }
        }

        @Test
        @WithMockUser(username = "firstname.latsname@test.pl")
        fun `should return BAD REQUEST status when try to borrow a book and books does not exist in db`() {
            // given
            val isbn = "100-0-00-000000-1"

            // when
            val performPost = mockMvc.post("$BASE_URL?isbn=$isbn") {
                contentType = APPLICATION_JSON
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }

        @Test
        @WithMockUser(username = "test.latsname@test.pl")
        fun `should return BAD REQUEST status when try to borrow a book and user does not exist in db`() {
            // given
            val isbn = "100-0-00-000000-1"

            // when
            val performPost = mockMvc.post("$BASE_URL?isbn=$isbn") {
                contentType = APPLICATION_JSON
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }

    }

    @Nested
    @DisplayName("ReturnBook")
    @TestInstance(PER_METHOD)
    inner class ReturnBook {

        @Test
        @WithMockUser(username = "firstname.latsname@test.pl")
        fun `should return OK status when return a book`() {
            // given
            val isbn = "000-0-00-000000-1"
            service.borrowBook(isbn)

            // when
            val performPost = mockMvc.post("$BASE_URL/return?isbn=$isbn") {
                contentType = APPLICATION_JSON
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isOk() } }
        }

        @Test
        @WithMockUser(username = "firstname.latsname@test.pl")
        fun `should return BAD REQUEST status when book does not borrowed`() {
            // given
            val isbn = "000-0-00-000000-1"

            // when
            val performPost = mockMvc.post("$BASE_URL/return?isbn=$isbn") {
                contentType = APPLICATION_JSON
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }
    }

    @Nested
    @DisplayName("BorrowDetails")
    @TestInstance(PER_METHOD)
    inner class BorrowDetails {

        @Test
        @WithMockUser(username = "firstname.latsname@test.pl")
        fun `should return OK status when return book borrow details`() {
            // given
            val isbn = "000-0-00-000000-1"
            service.borrowBook(isbn)

            // when
            val performPost = mockMvc.get("$BASE_URL?isbn=$isbn") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(
                    BorrowDetailsFull(
                        student = Student(id = 1L, firstname = "Firstname", lastname = "Lastname"),
                        book = Book(
                            isbn = "000-0-00-000000-1",
                            title = "title-test",
                            author = "author-test",
                            category = Category.MOTIVATIONAL,
                            format = Format.PAPERBACK,
                            release = LocalDate.of(2016, 6, 23)
                        ),
                        borrowDate = LocalDate.now(),
                        returnDate = null,
                        status = Status.BORROWED
                    )
                )
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.student.id") { value(1L) }
                    jsonPath("$.student.firstname") { value("Firstname") }
                    jsonPath("$.student.lastname") { value("Lastname") }
                    jsonPath("$.book.isbn") { value("000-0-00-000000-1") }
                    jsonPath("$.book.title") { value("title-test") }
                    jsonPath("$.book.author") { value("author-test") }
                    jsonPath("$.book.category") { value(Category.MOTIVATIONAL.name) }
                    jsonPath("$.book.format") { value(Format.PAPERBACK.name) }
                    jsonPath("$.book.release") { value("2016-06-23") }
                    jsonPath("$.returnDate") { value(null) }
                    jsonPath("$.status") { value(Status.BORROWED.name) }
                }
        }

        @Test
        @WithMockUser(username = "firstname.latsname@test.pl")
        fun `should return BAD REQUEST status when borrow details does not exist`() {
            // given
            val isbn = "000-0-00-000000-1"

            // when
            val performPost = mockMvc.get("$BASE_URL?isbn=$isbn") {
                contentType = APPLICATION_JSON
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }


    }

    private companion object {
        const val BASE_URL = "/api/v1/borrow"
    }
}