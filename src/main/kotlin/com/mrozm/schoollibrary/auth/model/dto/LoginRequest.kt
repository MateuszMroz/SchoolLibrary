package com.mrozm.schoollibrary.auth.model.dto

data class LoginRequest(
        val email: String,
        val password: String
)