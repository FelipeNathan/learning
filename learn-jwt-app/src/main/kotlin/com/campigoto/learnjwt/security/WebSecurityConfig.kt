package com.campigoto.learnjwt.security

import com.campigoto.learnjwt.service.TokenAuthenticationService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.inject.Inject

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Inject
    lateinit var userDetailsService: UserDetailsService

    @Inject
    lateinit var tokenAuthenticationService: TokenAuthenticationService

    override fun configure(http: HttpSecurity?) {
        http?.csrf()
                ?.disable()
                ?.authorizeRequests()
                ?.antMatchers("/home", "/h2-console/**")?.permitAll()
                ?.antMatchers(HttpMethod.POST, "/login")?.permitAll()
                ?.anyRequest()?.authenticated()
                ?.and()
                ?.addFilterBefore(JWTLoginFilter("/login", authenticationManager(), tokenAuthenticationService),
                        UsernamePasswordAuthenticationFilter::class.java)
                ?.addFilterBefore(JWTAuthenticationFilter(tokenAuthenticationService),
                        UsernamePasswordAuthenticationFilter::class.java)

        http?.headers()?.frameOptions()?.sameOrigin();
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()
}