package com.mrozm.schoollibrary.auth.validator

import com.mrozm.schoollibrary.auth.model.dto.RegisterRequest
import org.springframework.stereotype.Component

interface IRegisterValidator {
    fun valid(data: RegisterRequest): Boolean
}

@Component
class RegisterValidator : IRegisterValidator {
    override fun valid(data: RegisterRequest): Boolean {
        // very basic validation
        return with(data) { firstname.isNotBlank() && lastname.isNotBlank() && email.isNotBlank() && password.isNotBlank() }
    }

}