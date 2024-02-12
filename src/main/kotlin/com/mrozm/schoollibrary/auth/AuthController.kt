package com.mrozm.schoollibrary.auth

import com.mrozm.schoollibrary.auth.model.dto.AccessResponse
import com.mrozm.schoollibrary.auth.model.dto.LoginRequest
import com.mrozm.schoollibrary.auth.model.dto.RegisterRequest
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authorization Controller", description = "Endpoints to register and login to school library.")
class AuthController(
    private val service: IAuthService
) {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Bearer Token", content = [
                    (Content(mediaType = "application/json", schema = Schema(implementation = AccessResponse::class)))]
            ),
            ApiResponse(responseCode = "400", description = "No valid data", content = [Content()])]
    )
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): AccessResponse {
        return service.register(request)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Bearer Token", content = [
                    (Content(mediaType = "application/json", schema = Schema(implementation = AccessResponse::class)))]
            ),
            ApiResponse(responseCode = "400", description = "No valid data", content = [Content()])]
    )
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): AccessResponse {
        return service.login(request)
    }
}