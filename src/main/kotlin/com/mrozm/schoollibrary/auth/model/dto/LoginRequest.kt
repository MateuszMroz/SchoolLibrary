package com.mrozm.schoollibrary.auth.model.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
        @NotBlank
        val email: String,
        @NotBlank
        val password: String
)