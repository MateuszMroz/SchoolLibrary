package com.mrozm.schoollibrary.auth.model.dto

data class RegisterRequest(
        val firstname: String,
        val lastname: String,
        val email: String,
        val password: String
)