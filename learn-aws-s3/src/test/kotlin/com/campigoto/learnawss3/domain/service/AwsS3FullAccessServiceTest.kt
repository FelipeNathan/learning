package com.campigoto.learnawss3.domain.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.Bucket
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.Upload
import com.campigoto.learnawss3.application.LearnAwsS3Application
import com.campigoto.learnawss3.application.exception.AwsObjectException
import com.campigoto.learnawss3.domain.valueObjects.AwsS3VO
import com.campigoto.learnawss3.domain.valueObjects.BucketType
import com.campigoto.learnawss3.infraestructure.configuration.aws.AwsFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.io.InputStream

@SpringBootTest
@ContextConfiguration(classes = [LearnAwsS3Application::class])
@ActiveProfiles("test")
class AwsS3FullAccessServiceTest {

    @InjectMocks
    lateinit var service: AwsS3Service

    @Mock
    lateinit var awsFactory: AwsFactory

    @Mock
    lateinit var client: AmazonS3

    @Mock
    lateinit var transferManager: TransferManager

    @Mock
    lateinit var upload: Upload

    @Mock
    lateinit var inputStream: InputStream

    @Value("\${aws.s3.bucket}")
    lateinit var bucket: String

    @Mock
    lateinit var buckets: MutableList<Bucket>

    @BeforeEach
    fun setup() {
        `when`(awsFactory.client(BucketType.FULL_ACCESS)).thenReturn(client)
        `when`(awsFactory.transferManager(BucketType.FULL_ACCESS)).thenReturn(transferManager)
        `when`(awsFactory.bucket(BucketType.FULL_ACCESS)).thenReturn(bucket)
        `when`(transferManager.upload(any())).thenReturn(upload)
    }

    @Test
    fun `should throw file is required`() {

        val vo = AwsS3VO()
        val exception = assertThrows(AwsObjectException::class.java) { service.store(vo) }
        assertEquals(exception.message, FILE_REQUIRED)
    }

    @Test
    fun `should throw file name is required`() {

        val vo = AwsS3VO(file = inputStream)
        val exception = assertThrows(AwsObjectException::class.java) { service.store(vo) }
        assertEquals(exception.message, NAME_REQUIRED)
    }

    @Test
    fun `should throw content type is required`() {

        val vo = AwsS3VO(fileName = FILE_NAME, file = inputStream)
        val exception = assertThrows(AwsObjectException::class.java) { service.store(vo) }
        assertEquals(exception.message, CONTENT_TYPE_REQUIRED)
    }

    @Test
    fun `should throw bucket name is required`() {

        `when`(awsFactory.bucket(BucketType.FULL_ACCESS)).thenReturn(null)
        val vo = AwsS3VO(fileName = FILE_NAME, file = inputStream, contentType = CONTENT_TYPE)

        val exception = assertThrows(AwsObjectException::class.java) { service.store(vo) }

        assertEquals(exception.message, BUCKET_NAME_REQUIRED)
    }

    @Test
    fun `when all configure then pass`() {
        val vo = AwsS3VO(FILE_NAME, FILE_SIZE, CONTENT_TYPE, inputStream)
        service.store(vo)
    }

    @Test
    fun `when load buckets then pass`() {
        `when`(client.listBuckets()).thenReturn(buckets)

        val listedBuckets = service.listBuckets(BucketType.FULL_ACCESS)

        assertEquals(buckets, listedBuckets)
    }

    companion object {
        const val FILE_REQUIRED = "File is required"
        const val NAME_REQUIRED = "File name is required"
        const val CONTENT_TYPE_REQUIRED = "Content type is required"
        const val BUCKET_NAME_REQUIRED = "Bucket name is required"

        const val FILE_NAME = "mockFileName"
        const val FILE_SIZE = 10000L
        const val CONTENT_TYPE = "image/jpg"
    }
}