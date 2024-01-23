package com.mrozm.schoollibrary.config.security.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders.BASE64
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

interface IJwtExtractor {
    fun extractEmail(token: String): String?
}

interface IJwtValidator {
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean
}

interface IJwtGenerator {
    fun generateToken(userDetails: UserDetails): String
    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String
}

@Service
class JwtService : IJwtGenerator, IJwtValidator, IJwtExtractor {

    override fun generateToken(userDetails: UserDetails): String = generateToken(emptyMap(), userDetails)

    override fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        return Jwts
                .builder()
                .claims()
                .empty()
                .add(extraClaims)
                .and()
                .subject(userDetails.username)
                .issuedAt(Date(System.currentTimeMillis()))
                .expiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignInKey())
                .compact()
    }

    override fun isTokenValid(token: String, userDetails: UserDetails): Boolean =
            extractClaim(token, Claims::getExpiration).before(Date())

    override fun extractEmail(token: String): String? = extractClaim(token, Claims::getSubject)

    private fun <T> extractClaim(token: String, claimResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .payload
    }

    private fun getSignInKey(): SecretKey = Keys.hmacShaKeyFor(BASE64.decode(SECRET_KEY))

    private companion object {
        const val EXPIRATION_TIME = 1000 * 120 // 2min
        const val SECRET_KEY = "642e57625d704d7d4e28754e7d6e6974583034252f4c6f56512e536550"
    }

}