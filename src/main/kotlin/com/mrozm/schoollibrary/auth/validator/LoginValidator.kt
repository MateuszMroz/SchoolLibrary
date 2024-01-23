package com.mrozm.schoollibrary.auth.validator

import com.mrozm.schoollibrary.auth.model.dto.LoginRequest
import org.springframework.stereotype.Component

interface ILoginValidator {
    fun valid(data: LoginRequest): Boolean
}

@Component
class LoginValidator : ILoginValidator {
    override fun valid(data: LoginRequest): Boolean {
        // very basic validation
        return with(data) { email.isNotBlank() && password.isNotBlank() }
    }

}