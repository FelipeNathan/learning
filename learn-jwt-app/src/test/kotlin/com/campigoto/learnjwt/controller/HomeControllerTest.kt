package com.campigoto.learnjwt.controller

import com.campigoto.learnjwt.security.AccountCredentials
import com.campigoto.learnjwt.service.JwtUtils
import com.campigoto.learnjwt.service.TokenAuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.inject.Inject

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Inject
    lateinit var mvc: MockMvc

    @Inject
    lateinit var jwtUtils: JwtUtils

    @Test
    fun `home path should return ok status`() {
        mvc.perform(get("/home"))
                .andExpect(status().isOk)
                .andExpect(content().string("Hello World"))
    }

    @Test
    fun `should not authorize request with wrong user`() {
        val userDetail = AccountCredentials("someone", "someone")

        mvc.perform(
                post("/login").content(ObjectMapper().writeValueAsString(userDetail))
        ).andExpect(status().isForbidden)
    }

    @Test
    fun `should generate token`() {
        val userDetail = AccountCredentials("admin", "admin")

        val result = mvc.perform(
                post("/login").content(ObjectMapper().writeValueAsString(userDetail))
        ).andExpect(status().isOk).andReturn()

        val token = result.response.getHeaderValue(TokenAuthenticationService.HEADER_STRING).toString()
        assert(token.startsWith(TokenAuthenticationService.TOKEN_PREFIX))
    }

    @Test
    fun `should not authorize request without token`() {
        mvc.perform(get("/users")).andExpect(status().isForbidden)
    }
    
    @Test
    fun `should list fake users`() {

        val token = jwtUtils.generateToken("admin")
        val header = "${TokenAuthenticationService.TOKEN_PREFIX} $token"

        mvc.perform(get("/users").header(TokenAuthenticationService.HEADER_STRING, header))
                .andExpect(status().isOk)
                .andExpect(content().string(""" "users": [{"name": "lucas"}, {"name": "Felipe"}] """))
    }
}