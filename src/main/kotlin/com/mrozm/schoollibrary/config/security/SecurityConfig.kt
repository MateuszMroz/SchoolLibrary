package com.mrozm.schoollibrary.config.security

import com.mrozm.schoollibrary.config.security.filter.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
        private val jwtAuthFilter: JwtAuthFilter,
        private val authenticationProvider: AuthenticationProvider
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .csrf { it.disable() }
                .authorizeHttpRequests {
                    it.requestMatchers(PATTERN_TO_NOT_AUTHENTICATE)
                            .permitAll()
                    it.anyRequest().authenticated()
                }
                .sessionManagement {
                    it.sessionCreationPolicy(STATELESS)
                }
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    private companion object {
        const val PATTERN_TO_NOT_AUTHENTICATE = "/api/v1/auth/**"
    }
}