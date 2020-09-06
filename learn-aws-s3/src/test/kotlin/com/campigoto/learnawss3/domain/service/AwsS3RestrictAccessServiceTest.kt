package com.campigoto.learnawss3.domain.service

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.Bucket
import com.amazonaws.services.s3.transfer.TransferManager
import com.campigoto.learnawss3.application.LearnAwsS3Application
import com.campigoto.learnawss3.application.exception.AwsObjectException
import com.campigoto.learnawss3.domain.valueObjects.AwsS3VO
import com.campigoto.learnawss3.domain.valueObjects.BucketType
import com.campigoto.learnawss3.infraestructure.configuration.aws.AwsFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
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

    @InjectMocks
    lateinit var service: AwsS3Service

    @Mock
    lateinit var awsFactory: AwsFactory

    @Value("\${aws.s3.restrict.bucket}")
    lateinit var bucket: String

    @Qualifier("awsRestrictAccessClient")
    @SpyBean
    lateinit var client: AmazonS3

    @Autowired
    @Qualifier("awsRestrictAccessTransferManager")
    lateinit var transferManager: TransferManager

    @Mock
    lateinit var inputStream: InputStream

    @Mock
    lateinit var buckets: MutableList<Bucket>

    @BeforeEach
    fun setup() {
        `when`(awsFactory.client(BucketType.RESTRICT_ACCESS)).thenReturn(client)
        `when`(awsFactory.transferManager(BucketType.RESTRICT_ACCESS)).thenReturn(transferManager)
        `when`(awsFactory.bucket(BucketType.RESTRICT_ACCESS)).thenReturn(bucket)
    }

    @Test
    fun `should throw file is required`() {

        val vo = AwsS3VO(bucketType = BucketType.RESTRICT_ACCESS)
        val exception = Assertions.assertThrows(AwsObjectException::class.java) { service.store(vo) }
        Assertions.assertEquals(exception.message, FILE_REQUIRED)
    }

    @Test
    fun `should throw file name is required`() {

        val vo = AwsS3VO(file = inputStream, bucketType = BucketType.RESTRICT_ACCESS)
        val exception = Assertions.assertThrows(AwsObjectException::class.java) { service.store(vo) }
        Assertions.assertEquals(exception.message, NAME_REQUIRED)
    }

    @Test
    fun `should throw content type is required`() {

        val vo = AwsS3VO(fileName = FILE_NAME, file = inputStream, bucketType = BucketType.RESTRICT_ACCESS)
        val exception = Assertions.assertThrows(AwsObjectException::class.java) { service.store(vo) }
        Assertions.assertEquals(exception.message, CONTENT_TYPE_REQUIRED)
    }

    @Test
    fun `should throw sdk AmazonServiceException`() {

        val vo = AwsS3VO(FILE_NAME, FILE_SIZE, CONTENT_TYPE, inputStream, BucketType.RESTRICT_ACCESS)
        Assertions.assertThrows(AmazonServiceException::class.java) { service.store(vo) }
    }

    @Test
    fun `when load buckets then pass`() {
        doReturn(buckets).`when`(client).listBuckets()

        val listedBuckets = service.listBuckets(BucketType.RESTRICT_ACCESS)

        Assertions.assertEquals(buckets, listedBuckets)
    }


    companion object {
        const val FILE_REQUIRED = "File is required"
        const val NAME_REQUIRED = "File name is required"
        const val CONTENT_TYPE_REQUIRED = "Content type is required"

        const val FILE_NAME = "mockFileName"
        const val FILE_SIZE = 10000L
        const val CONTENT_TYPE = "image/jpg"
    }
}