package com.mrozm.schoollibrary.process.borrowdetails

import com.mrozm.schoollibrary.process.borrowdetails.model.dto.BorrowDetailsFull
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/borrow")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Borrow Book Controller", description = "Borrow and return a book")
class BorrowDetailsController(
    private val service: IBorrowDetailsService
) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleBadRequest(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, BAD_REQUEST)

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Borrow book",
                content = [(Content(mediaType = "application/json"))]
            ),
            ApiResponse(responseCode = "400", description = "No save", content = [Content()])]
    )
    @PostMapping
    fun borrowBook(@RequestParam("isbn") isbn: String): ResponseEntity<Any> {
        val result = service.borrowBook(isbn)
        return if (result >= 1) ResponseEntity(OK) else ResponseEntity(BAD_REQUEST)
    }

    @PostMapping("/return")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Return borrowed book",
                content = [(Content(mediaType = "application/json"))]
            ),
            ApiResponse(responseCode = "400", description = "No borrow details in database", content = [Content()])]
    )
    fun returnBook(@RequestParam("isbn") isbn: String): ResponseEntity<Any> {
        service.returnBook(isbn)
        return ResponseEntity(OK)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Return borrowed book details",
                content = [(Content(mediaType = "application/json"))]
            ),
            ApiResponse(responseCode = "400", description = "No borrow details in database", content = [Content()])]
    )
    @GetMapping
    fun findBorrowBookDetails(@RequestParam("isbn") isbn: String): ResponseEntity<BorrowDetailsFull> {
        return ResponseEntity(service.findBorrowDetails(isbn), OK)
    }
}