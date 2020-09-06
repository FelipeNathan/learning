package com.campigoto.learnawss3.application.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FullAccessControllerIntegrationTest {

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @Test
    fun `when file is null throw exception`() {

        val multipartFile = MockMultipartFile("file", null, MediaType.TEXT_PLAIN_VALUE, null)
        val mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()

        mockMvc.perform(
                multipart("/full-access/store", multipartFile.bytes)
                        .file(multipartFile)
        ).andExpect(content().string(FILE_REQUIRED))
    }

    companion object {
        const val FILE_REQUIRED = "File is required"
    }
}