package com.mrozm.schoollibrary.auth.model.dto

import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
        @NotBlank
        val firstname: String,
        @NotBlank
        val lastname: String,
        @NotBlank
        val email: String,
        @NotBlank
        val password: String
)