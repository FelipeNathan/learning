package com.campigoto.learnjwt.security

import com.campigoto.learnjwt.service.TokenAuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.stereotype.Component
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTLoginFilter(url: String
                     , authManager: AuthenticationManager
                     , tokenAuthenticationService: TokenAuthenticationService)
    : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(url, HttpMethod.POST.name)) {

    private var tokenAuthenticationService: TokenAuthenticationService

    init {
        this.authenticationManager = authManager
        this.tokenAuthenticationService = tokenAuthenticationService
    }

    override fun attemptAuthentication(req: HttpServletRequest?, res: HttpServletResponse?): Authentication? {
        val credentials = ObjectMapper().readValue(req?.inputStream, AccountCredentials::class.java)

        return try {
            authenticationManager?.authenticate(
                    UsernamePasswordAuthenticationToken(
                            credentials.username,
                            credentials.password,
                            Collections.emptyList()
                    )
            )
        } catch (ex: Exception) {
            res?.status = HttpStatus.FORBIDDEN.value()
            null
        }
    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {
        tokenAuthenticationService.addAuthentication(response, authResult?.name)
    }
}