package com.mrozm.schoollibrary.auth

import com.mrozm.schoollibrary.auth.model.dto.RegisterRequest
import com.mrozm.schoollibrary.auth.model.entity.Role.USER
import com.mrozm.schoollibrary.auth.model.entity.Student
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

interface IAuthMapper {
    fun mapTo(request: RegisterRequest): Student
}

@Component
class AuthMapper(
        private val passwordEncoder: PasswordEncoder
) : IAuthMapper {

    override fun mapTo(request: RegisterRequest): Student = Student(
            firstname = request.firstname,
            lastname = request.lastname,
            email = request.email,
            pass = passwordEncoder.encode(request.password),
            role = USER
    )
}