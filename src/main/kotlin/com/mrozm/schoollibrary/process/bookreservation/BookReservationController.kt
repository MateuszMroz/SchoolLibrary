package com.mrozm.schoollibrary.process.bookreservation

import com.mrozm.schoollibrary.process.bookreservation.model.dto.BookReservationResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/details")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Borrow Book Controller", description = "Borrow and return a book")
class BookReservationController(
    private val service: IBookReservationService
) {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Return borrowed book details",
                content = [(Content(mediaType = "application/json"))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "No borrow details in database",
                content = [Content()]
            )]
    )
    @GetMapping
    fun findBorrowBookDetails(@RequestParam("isbn") isbn: String): BookReservationResponse {
        return service.findBookReservationDetails(isbn)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Borrow book",
                content = [(Content(mediaType = "application/json"))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Save failed or book already borrowed",
                content = [Content()]
            )]
    )
    @PostMapping("/reserve")
    fun reserveBook(@RequestParam("isbn") isbn: String) {
        service.reserveBook(isbn)
    }

    @PostMapping("/return")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Return borrowed book",
                content = [(Content(mediaType = "application/json"))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "No borrow details in database",
                content = [Content()]
            )]
    )
    fun returnBook(@RequestParam("isbn") isbn: String) {
        service.returnBook(isbn)
    }
}