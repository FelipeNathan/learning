package com.campigoto.learnjwt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class LearnJwtApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
	runApplication<LearnJwtApplication>(*args)
}
