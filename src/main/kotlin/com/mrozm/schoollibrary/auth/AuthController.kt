package com.mrozm.schoollibrary.auth

import com.mrozm.schoollibrary.auth.model.dto.AccessResponse
import com.mrozm.schoollibrary.auth.model.dto.LoginRequest
import com.mrozm.schoollibrary.auth.model.dto.RegisterRequest
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authorization", description = "Endpoints to register and login to school library.")
class AuthController(
        private val service: IAuthService
) {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
            ResponseEntity(e.message, BAD_REQUEST)

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Bearer Token", content = [
            (Content(mediaType = "application/json", schema = Schema(implementation = AccessResponse::class)))]),
        ApiResponse(responseCode = "400", description = "No valid data", content = [Content()])]
    )
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AccessResponse> {
        return ResponseEntity.ok(service.register(request))
    }

    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Bearer Token", content = [
            (Content(mediaType = "application/json", schema = Schema(implementation = AccessResponse::class)))]),
        ApiResponse(responseCode = "400", description = "No valid data", content = [Content()])]
    )
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AccessResponse> {
        return ResponseEntity.ok(service.login(request))
    }
}