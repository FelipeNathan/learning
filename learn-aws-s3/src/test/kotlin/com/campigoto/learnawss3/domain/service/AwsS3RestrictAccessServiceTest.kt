package com.campigoto.learnawss3.domain.service

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.transfer.TransferManager
import com.campigoto.learnawss3.application.LearnAwsS3Application
import com.campigoto.learnawss3.application.exception.AwsObjectException
import com.campigoto.learnawss3.domain.valueObjects.AwsS3VO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.io.InputStream

/**
 * For this test, you must have an user with no write permission
 * */
@SpringBootTest
@ContextConfiguration(classes = [LearnAwsS3Application::class])
@ActiveProfiles("test")
class AwsS3RestrictAccessServiceTest {

    lateinit var service: AwsS3RestrictAccessService

    @Value("\${aws.s3.restrict.bucket}")
    lateinit var bucket: String

    @Autowired
    @Qualifier("restrictAccessClient")
    lateinit var client: AmazonS3

    @Autowired
    @Qualifier("restrictAccessTransferManager")
    lateinit var transferManager: TransferManager

    @Mock
    lateinit var inputStream: InputStream

    @BeforeEach
    fun config() {
        service = AwsS3RestrictAccessService(bucket, client, transferManager)
    }

    @Test
    fun `should throw file is required`() {

        val vo = AwsS3VO()
        val exception = Assertions.assertThrows(AwsObjectException::class.java) { service.store(vo) }
        Assertions.assertEquals(exception.message, FILE_REQUIRED)
    }

    @Test
    fun `should throw file name is required`() {

        val vo = AwsS3VO(file = inputStream)
        val exception = Assertions.assertThrows(AwsObjectException::class.java) { service.store(vo) }
        Assertions.assertEquals(exception.message, NAME_REQUIRED)
    }

    @Test
    fun `should throw content type is required`() {

        val vo = AwsS3VO(fileName = FILE_NAME, file = inputStream)
        val exception = Assertions.assertThrows(AwsObjectException::class.java) { service.store(vo) }
        Assertions.assertEquals(exception.message, CONTENT_TYPE_REQUIRED)
    }

    @Test
    fun `should throw sdk AmazonServiceException`() {

        val vo = AwsS3VO(FILE_NAME, 100, "image/jpeg", inputStream)
        Assertions.assertThrows(AmazonServiceException::class.java) { service.store(vo) }
    }

    companion object {
        const val FILE_REQUIRED = "File is required"
        const val NAME_REQUIRED = "File name is required"
        const val CONTENT_TYPE_REQUIRED = "Content type is required"
        const val FILE_NAME = "mockFileName"
    }
}