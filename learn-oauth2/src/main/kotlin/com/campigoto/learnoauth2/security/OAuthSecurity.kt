package com.campigoto.learnoauth2.security

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
class OAuthSecurity : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {

        http?.csrf()?.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

        http?.authorizeRequests {

            val permited = arrayOf("/", "/error", "/webjars/**")
            it.antMatchers(*permited).permitAll()
                    .anyRequest().authenticated()

        }?.exceptionHandling {

            it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))

        }?.oauth2Login { o ->

            o.failureHandler { request, response, ex ->
                request.session.setAttribute("error.message", ex.message)
            }

        }?.logout {
            it.logoutSuccessUrl("/").permitAll()
        }
    }
}