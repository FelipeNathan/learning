package com.campigoto.learnawss3.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.campigoto"])
class LearnAwsS3Application

fun main(args: Array<String>) {
	runApplication<LearnAwsS3Application>(*args)
}
