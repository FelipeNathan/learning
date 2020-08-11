package com.campigoto.learnjwt.service

import io.jsonwebtoken.ExpiredJwtException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import javax.inject.Inject

@SpringBootTest
class JwtUtilsTest {

    @Inject
    lateinit var jwtUtils: JwtUtils

    @Test
    fun `should throw expired exception`() {

        jwtUtils.expirationTime = 10

        val token = jwtUtils.generateToken(SUBJECT)
        Thread.sleep(1000)

        assertThrows<ExpiredJwtException> { jwtUtils.getSubject(token) }
    }

    @Test
    fun `should extract subject with success`() {

        jwtUtils.expirationTime = 2000

        val token = jwtUtils.generateToken(SUBJECT)
        Thread.sleep(1000)

        assertEquals(SUBJECT, jwtUtils.getSubject(token))
    }

    companion object {
        const val SUBJECT = "subject"
    }
}