package com.campigoto.learnoauth2.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
class UserController {

    @GetMapping("/user")
    fun user(@AuthenticationPrincipal principal: OAuth2User) = Collections.singletonMap("name", principal.name)

    @GetMapping("/error")
    fun error(request: HttpServletRequest): String? {

        val errorMessage = request.session.getAttribute("error.message")
        request.session.removeAttribute("error.message")

        return errorMessage?.toString()
    }
}