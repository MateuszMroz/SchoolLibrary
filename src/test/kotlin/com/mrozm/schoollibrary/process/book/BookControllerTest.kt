package com.mrozm.schoollibrary.process.book

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrozm.schoollibrary.process.book.model.dto.Book
import com.mrozm.schoollibrary.process.book.model.dto.Category.ART
import com.mrozm.schoollibrary.process.book.model.dto.Format.PAPERBACK
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
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.*
import java.time.LocalDate

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc(addFilters = false) // is it the best way to skip authentication ?
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@AutoConfigureMybatis
class BookControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var service: IBookService

    private val objectMapper: ObjectMapper = createObjectMapper()

    private val book1 = Book(
        isbn = "978-3-16-148410-0",
        title = "title-test",
        author = "author-test",
        category = ART,
        format = PAPERBACK,
        release = LocalDate.of(2024, 1, 24)
    )

    private val book2 = Book(
        isbn = "978-3-16-148410-1",
        title = "title-test-2",
        author = "author-test-2",
        category = ART,
        format = PAPERBACK,
        release = LocalDate.of(2000, 1, 24)
    )

    @Nested
    @DisplayName("SaveBook")
    @TestInstance(PER_METHOD)
    inner class SaveBook {
        @Test
        fun `should return CREATED when save book successfully`() {
            // given/when
            val performPost = mockMvc.post(BASE_URL) {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(book1)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isCreated() } }
        }

        @Test
        fun `should provide book when save book successfully`() {
            // given/when
            val performPost = mockMvc.post(BASE_URL) {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(book1)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    jsonPath("$.isbn") { value("978-3-16-148410-0") }
                    jsonPath("$.title") { value("title-test") }
                    jsonPath("$.author") { value("author-test") }
                    jsonPath("$.category") { value(ART.name) }
                    jsonPath("$.format") { value(PAPERBACK.name) }
                    jsonPath("$.release") { value("2024-01-24") }
                }
        }
    }

    @Nested
    @DisplayName("FindBook")
    @TestInstance(PER_METHOD)
    inner class FindBook {

        @Test
        fun `should return OK status when try to fetch book with specific isbn`() {
            // given
            service.save(book1)

            // when
            val performGet = mockMvc.get("$BASE_URL/978-3-16-148410-0") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(book1)
            }

            // then
            performGet
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                }
        }

        @Test
        fun `should return NOT FOUND when no books with specific isbn`() {
            // given
            val isbn = "no-isbn-in-db"

            // when
            val performGet = mockMvc.get("$BASE_URL/$isbn") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(book1)
            }

            // then
            performGet
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }

        @Test
        fun `should provide book when try to fetch book with specific isbn`() {
            // given
            val isbn = "978-3-16-148410-0"
            service.save(book1)

            // when
            val performGet = mockMvc.get("$BASE_URL/$isbn") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(book1)
            }

            // then
            performGet
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.isbn") { value(isbn) }
                    jsonPath("$.title") { value("title-test") }
                    jsonPath("$.author") { value("author-test") }
                    jsonPath("$.category") { value(ART.name) }
                    jsonPath("$.format") { value(PAPERBACK.name) }
                    jsonPath("$.release") { value("2024-01-24") }
                }
        }
    }

    @Nested
    @DisplayName("FindAllBooks")
    @TestInstance(PER_METHOD)
    inner class FindAllBooks {

        @Test
        fun `should return OK status when try fetch all books`() {
            // given
            val books = listOf(book1, book2)
            service.save(book1)
            service.save(book2)


            // when
            val performGet = mockMvc.get(BASE_URL) {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(books)
            }

            // then
            performGet
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                }
        }

        @Test
        fun `should provide lis of books when try to fetch all books`() {
            // given
            val books = listOf(book1, book2)
            service.save(book1)
            service.save(book2)

            // when
            val performGet = mockMvc.get(BASE_URL) {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(books)
            }

            // then
            performGet
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$[0].isbn") { value("978-3-16-148410-0") }
                    jsonPath("$[0].title") { value("title-test") }
                    jsonPath("$[0].author") { value("author-test") }
                    jsonPath("$[0].category") { value(ART.name) }
                    jsonPath("$[0].format") { value(PAPERBACK.name) }
                    jsonPath("$[0].release") { value("2024-01-24") }
                }
        }

    }

    @Nested
    @DisplayName("UpdatedBook")
    @TestInstance(PER_METHOD)
    inner class UpdatedBook {

        @Test
        fun `should return OK when update process has been occur`() {
            // given
            val isbn = "978-3-16-148410-0"
            val book = book1.copy(title = "title-update", author = "author-update")
            service.save(book1)

            // when
            val performPut = mockMvc.put("$BASE_URL/$isbn") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(book)
            }

            // then
            performPut
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                }
        }

        @Test
        fun `should return BAD REQUEST when try to update object with wrong isbn`() {
            // given
            val isbn = "wrong isbn"
            val book = book1.copy(title = "title-update", author = "author-update")
            service.save(book1)

            // when
            val performPut = mockMvc.put("$BASE_URL/$isbn") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(book)
            }

            // then
            performPut
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("DeleteBook")
    @TestInstance(PER_METHOD)
    inner class DeleteBook {

        @Test
        fun `should return NO CONTENT when delete book with specific isbn`() {
            // given
            val isbn = "978-3-16-148410-0"
            service.save(book1)

            // when
            val performDelete = mockMvc.delete("$BASE_URL/$isbn") {
                contentType = APPLICATION_JSON
            }

            // then
            performDelete
                .andDo { print() }
                .andExpect { status { isNoContent() } }
        }
    }

    companion object {
        const val BASE_URL = "/api/v1/book"
    }

}