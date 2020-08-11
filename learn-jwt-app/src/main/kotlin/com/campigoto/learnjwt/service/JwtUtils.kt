package com.campigoto.learnjwt.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {

    @Value("\${jwt.secret}")
    lateinit var secret: String

    @Value("\${jwt.expiration.time}")
    var expirationTime: Long = 0

    fun generateToken(subject: String?): String? = Jwts.builder()
            .setSubject(subject)
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()


    fun getSubject(token: String?): String? = extractClaims(token).subject ?: null

    fun extractClaims(token: String?): Claims = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
}