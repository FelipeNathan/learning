package com.campigoto.learnjwt.service

import com.campigoto.learnjwt.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import javax.inject.Inject

@Service
class CustomUserDetailsService : UserDetailsService {

    @Inject
    lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails? {

        val user = userRepository.findByUsername(username)

        return if (user == null) null
        else AppUserDetail(user.username!!, user.password)

    }
}