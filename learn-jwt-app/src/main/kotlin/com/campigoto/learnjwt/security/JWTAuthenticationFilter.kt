package com.campigoto.learnjwt.security

import com.campigoto.learnjwt.service.TokenAuthenticationService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JWTAuthenticationFilter(private val tokenAuthenticationService: TokenAuthenticationService) : GenericFilterBean() {

    override fun doFilter(req: ServletRequest?, res: ServletResponse?, chain: FilterChain?) {

        val auth = tokenAuthenticationService.getAuthentication(req as HttpServletRequest)

        SecurityContextHolder.getContext().authentication = auth

        chain?.doFilter(req, res)
    }
}