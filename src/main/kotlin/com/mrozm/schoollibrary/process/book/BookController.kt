package com.mrozm.schoollibrary.process.book

import com.mrozm.schoollibrary.process.book.model.dto.Book
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/book")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Book Controller", description = "CRUD functionality for books")
class BookController(
    private val service: IBookService
) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleBadRequest(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, NOT_FOUND)

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Save book", content = [
                    (Content(mediaType = "application/json", schema = Schema(implementation = Book::class)))]
            ),
            ApiResponse(responseCode = "400", description = "Problem with save", content = [Content()])]
    )
    @PostMapping
    fun save(@RequestBody book: Book): ResponseEntity<Book> {
        return service.save(book).let {
            when {
                it >= 1 -> ResponseEntity(book, CREATED)
                else -> ResponseEntity(BAD_REQUEST)
            }
        }
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Update book", content = [
                    (Content(mediaType = "application/json", schema = Schema(implementation = Book::class)))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Problem with update book because of isbn wrong number",
                content = [Content()]
            )]
    )
    @PutMapping("/{isbn}")
    fun update(@PathVariable("isbn") isbn: String, @RequestBody book: Book): ResponseEntity<Book> {
        if (isbn != book.isbn)
            return ResponseEntity(BAD_REQUEST)

        service.update(book)

        return ResponseEntity(OK)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Find book by isbn", content = [
                    (Content(mediaType = "application/json", schema = Schema(implementation = Book::class)))]
            )]
    )
    @GetMapping("/{isbn}")
    fun findByIsbn(@PathVariable("isbn") isbn: String): ResponseEntity<Book> {
        return ResponseEntity(service.findByIsbn(isbn), OK)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Find all books", content = [
                    (Content(
                        mediaType = "application/json", array = (
                                ArraySchema(schema = Schema(implementation = Book::class)))
                    ))]
            )]
    )
    @GetMapping
    fun findAll(): ResponseEntity<List<Book>> {
        return ResponseEntity(service.findAll(), OK)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204", description = "Delete book by isbn", content = [
                    (Content(mediaType = "application/json"))]
            )]
    )
    @DeleteMapping("/{isbn}")
    fun delete(@PathVariable("isbn") isbn: String): ResponseEntity<Book> {
        service.delete(isbn)
        return ResponseEntity(NO_CONTENT)
    }
}