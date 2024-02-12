package com.mrozm.schoollibrary.auth

import com.mrozm.schoollibrary.auth.model.dto.AccessResponse
import com.mrozm.schoollibrary.auth.model.dto.LoginRequest
import com.mrozm.schoollibrary.auth.model.dto.RegisterRequest
import com.mrozm.schoollibrary.auth.model.entity.StudentEntity
import com.mrozm.schoollibrary.auth.validator.ILoginValidator
import com.mrozm.schoollibrary.auth.validator.IRegisterValidator
import com.mrozm.schoollibrary.config.security.service.IJwtGenerator
import org.apache.coyote.BadRequestException
import org.apache.ibatis.javassist.NotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

interface IAuthService {

    fun register(request: RegisterRequest): AccessResponse
    fun login(request: LoginRequest): AccessResponse
    fun findByEmail(email: String): StudentEntity
}

@Service
class AuthService(
    private val repository: AuthRepository,
    private val mapper: IAuthMapper,
    private val registerValidator: IRegisterValidator,
    private val loginValidator: ILoginValidator,
    private val jwtGenerator: IJwtGenerator,
    private val authenticationManager: AuthenticationManager,
) : IAuthService {

    override fun register(request: RegisterRequest): AccessResponse {
        if (!registerValidator.valid(request))
            throw BadRequestException("User data $request are not valid.")

        val user = mapper.mapTo(request)
        repository.save(user)

        return AccessResponse(jwtGenerator.generateToken(user))
    }

    override fun login(request: LoginRequest): AccessResponse {
        if (!loginValidator.valid(request))
            throw BadRequestException("User data $request are not valid.")

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )

        // TODO Check password
        val user = repository.findByEmail(request.email)
            ?: throw BadRequestException("User ${request.email} not found in database.")

        return AccessResponse(jwtGenerator.generateToken(user))

    }

    // TODO: Move to separate student service
    override fun findByEmail(email: String): StudentEntity {
        return repository.findByEmail(email) ?: throw NotFoundException("User: $email not found in database.")
    }
}