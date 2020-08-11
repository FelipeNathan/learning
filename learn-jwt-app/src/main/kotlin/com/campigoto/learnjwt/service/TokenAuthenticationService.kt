package com.campigoto.learnjwt.service

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class TokenAuthenticationService {

    @Inject
    lateinit var jwtUtils: JwtUtils

    fun addAuthentication(res: HttpServletResponse?, username: String?) {
        val jwt = jwtUtils.generateToken(username)
        res?.addHeader(HEADER_STRING, "$TOKEN_PREFIX $jwt")
    }

    fun getAuthentication(req: HttpServletRequest?): Authentication? {

        val token = req?.getHeader(HEADER_STRING)?.replace(TOKEN_PREFIX, "") ?: return null

        val user = jwtUtils.getSubject(token)

        return UsernamePasswordAuthenticationToken(user, null, Collections.emptyList())
    }

    companion object {
        const val HEADER_STRING = "Authorization"
        const val TOKEN_PREFIX = "Bearer"
    }
}