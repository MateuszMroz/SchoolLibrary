package com.mrozm.schoollibrary.auth

import com.mrozm.schoollibrary.auth.model.dto.AccessResponse
import com.mrozm.schoollibrary.auth.model.dto.LoginRequest
import com.mrozm.schoollibrary.auth.model.dto.RegisterRequest
import com.mrozm.schoollibrary.auth.validator.ILoginValidator
import com.mrozm.schoollibrary.auth.validator.IRegisterValidator
import com.mrozm.schoollibrary.config.security.service.IJwtGenerator
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

interface IAuthService {
    fun register(request: RegisterRequest): AccessResponse
    fun login(request: LoginRequest): AccessResponse
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
            throw IllegalArgumentException("User data $request are not valid.")

        val user = mapper.mapTo(request)
        val rowAffected = repository.save(user)

        if (rowAffected > 0) {
            return AccessResponse(jwtGenerator.generateToken(user))
        } else throw IllegalArgumentException("User $request do not save in database.")

    }

    override fun login(request: LoginRequest): AccessResponse {
        if (!loginValidator.valid(request))
            throw IllegalArgumentException("User data $request are not valid.")

        authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        request.email,
                        request.password
                )
        )

        val user = repository.findByEmail(request.email)
                ?: throw UsernameNotFoundException("User ${request.email} not found in database.")

        return AccessResponse(jwtGenerator.generateToken(user))

    }
}