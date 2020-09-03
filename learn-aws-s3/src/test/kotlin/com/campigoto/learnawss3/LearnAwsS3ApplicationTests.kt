package com.campigoto.learnawss3

import com.campigoto.learnawss3.application.LearnAwsS3Application
import com.campigoto.learnawss3.application.controller.HomeController
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [LearnAwsS3Application::class])
class LearnAwsS3ApplicationTests {

    @Autowired
    lateinit var homeController: HomeController

    @Test
    fun contextLoads() {
        assertNotNull(homeController)
    }

}
