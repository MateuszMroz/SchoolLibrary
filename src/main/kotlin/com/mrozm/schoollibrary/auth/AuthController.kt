package com.mrozm.schoollibrary.auth

import com.mrozm.schoollibrary.auth.model.dto.AccessResponse
import com.mrozm.schoollibrary.auth.model.dto.LoginRequest
import com.mrozm.schoollibrary.auth.model.dto.RegisterRequest
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
        private val service: IAuthService
) {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
            ResponseEntity(e.message, BAD_REQUEST)

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AccessResponse> {
        return ResponseEntity.ok(service.register(request))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AccessResponse> {
        return ResponseEntity.ok(service.login(request))
    }
}