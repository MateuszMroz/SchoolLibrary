package com.mrozm.schoollibrary.process.book

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrozm.schoollibrary.process.book.model.dto.Category.ADVENTURE
import com.mrozm.schoollibrary.process.book.model.dto.Category.MOTIVATIONAL
import com.mrozm.schoollibrary.process.book.model.dto.Format.PAPERBACK
import com.mrozm.schoollibrary.utils.BOOK_1
import com.mrozm.schoollibrary.utils.BOOK_2
import com.mrozm.schoollibrary.utils.TestDataGenerator
import com.mrozm.schoollibrary.utils.TestDataGenerator.Companion.BOOK_ISBN
import com.mrozm.schoollibrary.utils.TestDataGenerator.Companion.BOOK_ISBN_2
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
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc(addFilters = false) // is it the best way to skip authentication ?
@Transactional
class BookControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var generator: TestDataGenerator

    private val objectMapper: ObjectMapper = createObjectMapper()

    @Nested
    @DisplayName("SaveBook")
    @TestInstance(PER_METHOD)
    inner class SaveBook {

        @Test
        fun `should return OK when save book successfully`() {
            // given/when
            val performPost = mockMvc.post(BASE_URL) {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(BOOK_1)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isOk() } }
        }

    }

    @Nested
    @DisplayName("FindBook")
    @TestInstance(PER_METHOD)
    inner class FindBook {

        @Test
        fun `should return OK status when try to fetch book with specific isbn`() {
            // given
            generator.addBook(BOOK_1)

            // when
            val performGet = mockMvc.get("$BASE_URL/$BOOK_ISBN") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(BOOK_1)
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
                content = objectMapper.writeValueAsString(BOOK_1)
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
            generator.addBook(BOOK_1)

            // when
            val performGet = mockMvc.get("$BASE_URL/$BOOK_ISBN") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(BOOK_1)
            }

            // then
            performGet
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.isbn") { value(BOOK_ISBN) }
                    jsonPath("$.title") { value("The Power of Positive Thinking") }
                    jsonPath("$.author") { value("Norman Vincent Peale") }
                    jsonPath("$.category") { value(MOTIVATIONAL.name) }
                    jsonPath("$.format") { value(PAPERBACK.name) }
                    jsonPath("$.release") { value("2023-10-10") }
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
            generator.addBook(BOOK_1, BOOK_2)

            // when
            val performGet = mockMvc.get(BASE_URL) {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(listOf(BOOK_1, BOOK_2))
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
            generator.addBook(BOOK_1, BOOK_2)

            // when
            val performGet = mockMvc.get(BASE_URL) {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(listOf(BOOK_1, BOOK_2))
            }

            // then
            performGet
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$[0].isbn") { value(BOOK_ISBN) }
                    jsonPath("$[0].title") { value("The Power of Positive Thinking") }
                    jsonPath("$[0].author") { value("Norman Vincent Peale") }
                    jsonPath("$[0].category") { value(MOTIVATIONAL.name) }
                    jsonPath("$[0].format") { value(PAPERBACK.name) }
                    jsonPath("$[0].release") { value("2023-10-10") }
                    jsonPath("$[1].isbn") { value(BOOK_ISBN_2) }
                    jsonPath("$[1].title") { value("Don Quixote") }
                    jsonPath("$[1].author") { value("Miguel de Cervantes") }
                    jsonPath("$[1].category") { value(ADVENTURE.name) }
                    jsonPath("$[1].format") { value(PAPERBACK.name) }
                    jsonPath("$[1].release") { value("2000-10-10") }
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
            val book = BOOK_1.copy(title = "title-update", author = "author-update")
            generator.addBook(BOOK_1)

            // when
            val performPut = mockMvc.put("$BASE_URL/$BOOK_ISBN") {
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
        fun `should return NOT FOUND when try to update object and book does not exist`() {
            // given
            val book = BOOK_1.copy(title = "title-update", author = "author-update")
            generator.addBook(BOOK_2)

            // when
            val performPut = mockMvc.put("$BASE_URL/$BOOK_ISBN") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(book)
            }

            // then
            performPut
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }

        @Test
        fun `should return NOT FOUND when try to update object with wrong isbn`() {
            // given
            val isbn = "wrong isbn"
            val book = BOOK_1.copy(title = "title-update", author = "author-update")
            generator.addBook(BOOK_1)

            // when
            val performPut = mockMvc.put("$BASE_URL/$isbn") {
                contentType = APPLICATION_JSON
                content = objectMapper.writeValueAsString(book)
            }

            // then
            performPut
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }

    }

    @Nested
    @DisplayName("DeleteBook")
    @TestInstance(PER_METHOD)
    inner class DeleteBook {

        @Test
        fun `should return OK when delete book with specific isbn`() {
            // given
            generator.addBook(BOOK_1)

            // when
            val performDelete = mockMvc.delete("$BASE_URL/$BOOK_ISBN") {
                contentType = APPLICATION_JSON
            }

            // then
            performDelete
                .andDo { print() }
                .andExpect { status { isOk() } }
        }

    }

    companion object {
        const val BASE_URL = "/api/v1/book"
    }
}