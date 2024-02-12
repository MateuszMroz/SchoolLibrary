package com.mrozm.schoollibrary.auth

import com.mrozm.schoollibrary.auth.model.dto.RegisterRequest
import com.mrozm.schoollibrary.auth.model.entity.Role.USER
import com.mrozm.schoollibrary.auth.model.entity.StudentEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

interface IAuthMapper {
    fun mapTo(request: RegisterRequest): StudentEntity
}

@Component
class AuthMapper(
    private val passwordEncoder: PasswordEncoder
) : IAuthMapper {

    override fun mapTo(request: RegisterRequest): StudentEntity = StudentEntity(
        uuid = UUID.randomUUID().toString(), // move to service when uuid will be necessary
        firstname = request.firstname,
        lastname = request.lastname,
        email = request.email,
        pass = passwordEncoder.encode(request.password),
        role = USER
    )
}