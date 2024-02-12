package com.mrozm.schoollibrary.process.book

import com.mrozm.schoollibrary.process.book.model.dto.BookResponse
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/book")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Book Controller", description = "CRUD functionality for books")
class BookController(
    private val service: IBookService
) {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Save book",
                content = [
                    (Content(mediaType = "application/json", schema = Schema(implementation = BookResponse::class)))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Problem with save",
                content = [Content()]
            )]
    )
    @PostMapping
    fun save(@RequestBody book: BookResponse) {
        service.save(book)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Update book",
                content = [
                    (Content(mediaType = "application/json", schema = Schema(implementation = BookResponse::class)))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Problem with update book because of isbn wrong number",
                content = [Content()]
            )]
    )
    @PutMapping("/{isbn}")
    fun update(@PathVariable("isbn") isbn: String, @RequestBody book: BookResponse) {
        service.update(isbn, book)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Find book by isbn",
                content = [(Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = BookResponse::class)
                ))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = [Content()]
            )]
    )
    @GetMapping("/{isbn}")
    fun findByIsbn(@PathVariable("isbn") isbn: String): BookResponse {
        return service.findByIsbn(isbn)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Find all books",
                content = [(Content(
                    mediaType = "application/json",
                    array = (ArraySchema(schema = Schema(implementation = BookResponse::class)))
                ))]
            )]
    )
    @GetMapping
    fun findAll(): List<BookResponse> {
        return service.findAll()
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Delete book by isbn",
                content = [(Content(mediaType = "application/json"))]
            )]
    )
    @DeleteMapping("/{isbn}")
    fun delete(@PathVariable("isbn") isbn: String) {
        service.delete(isbn)
    }
}