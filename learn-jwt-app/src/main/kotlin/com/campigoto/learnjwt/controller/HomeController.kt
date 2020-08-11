package com.campigoto.learnjwt.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

    @GetMapping("/home")
    fun home() = "Hello World"

    @PostMapping("/login")
    fun login() = "Post Login"

    @GetMapping("/users")
    fun users() = """ "users": [{"name": "lucas"}, {"name": "Felipe"}] """
}