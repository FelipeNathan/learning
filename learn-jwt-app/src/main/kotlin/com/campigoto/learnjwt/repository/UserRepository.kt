package com.campigoto.learnjwt.repository

import com.campigoto.learnjwt.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String?): User?
}