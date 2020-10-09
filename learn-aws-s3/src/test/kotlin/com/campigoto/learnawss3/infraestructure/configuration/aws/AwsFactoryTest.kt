package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.Region
import com.amazonaws.services.s3.transfer.TransferManager
import com.campigoto.learnawss3.domain.valueObjects.AwsS3Properties
import com.campigoto.learnawss3.domain.valueObjects.BucketType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.test.util.ReflectionTestUtils

class AwsFactoryTest {

    private lateinit var factory: AwsFactory

    @Mock
    private lateinit var defaultConfiguration: AwsConfiguration

    @Mock
    private lateinit var temporaryConfiguration: AwsConfiguration

    @Mock
    private lateinit var athenaConfiguration: AwsAthenaConfiguration

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val builder = AwsClientBuilder()
        val defaultClient: AmazonS3 = builder.client(AwsS3Properties(DEFAULT_REGION, DEFAULT_BUCKET, DEFAULT_SECRET, DEFAULT_KEY))
        Mockito.`when`(defaultConfiguration.getBucketType()).thenReturn(BucketType.FULL_ACCESS)
        Mockito.`when`(defaultConfiguration.getClient()).thenReturn(defaultClient)
        Mockito.`when`(defaultConfiguration.getTransferManager()).thenReturn(builder.transferManager(defaultClient))
        Mockito.`when`(defaultConfiguration.getBucket()).thenReturn(DEFAULT_BUCKET)

        val temporaryClient: AmazonS3 = builder.client(AwsS3Properties(TEMPORARY_REGION, TEMPORARY_BUCKET, TEMPORARY_SECRET, TEMPORARY_KEY))
        Mockito.`when`(temporaryConfiguration.getBucketType()).thenReturn(BucketType.RESTRICT_ACCESS)
        Mockito.`when`(temporaryConfiguration.getClient()).thenReturn(temporaryClient)
        Mockito.`when`(temporaryConfiguration.getBucket()).thenReturn(TEMPORARY_BUCKET)
        Mockito.`when`(temporaryConfiguration.getTransferManager()).thenReturn(builder.transferManager(temporaryClient))

        factory = AwsFactory(mutableListOf(defaultConfiguration, temporaryConfiguration), athenaConfiguration)
        factory.init()
    }

    @Test
    fun `default BucketType should return default bucket name`() {
        val bucket: String? = factory.bucket(BucketType.FULL_ACCESS)

        assertEquals(bucket, DEFAULT_BUCKET)
    }

    @Test
    fun `default BucketType should return default client`() {
        val client: AmazonS3 = factory.client(BucketType.FULL_ACCESS)
        val credentialsProvider = ReflectionTestUtils.getField(client, "awsCredentialsProvider") as AWSCredentialsProvider?
        val expectedProvider: AWSCredentialsProvider = AWSStaticCredentialsProvider(BasicAWSCredentials(DEFAULT_KEY, DEFAULT_SECRET))

        assertThat(client.region).isEqualTo(Region.US_Standard)
        assertThat(credentialsProvider).isNotNull.usingRecursiveComparison().ignoringActualNullFields().isEqualTo(expectedProvider)
    }

    @Test
    fun `default BucketType should return default transfer manager`() {
        val transferManager: TransferManager = factory.transferManager(BucketType.FULL_ACCESS)
        val client: AmazonS3 = factory.client(BucketType.FULL_ACCESS)

        assertEquals(transferManager.amazonS3Client, client)
    }

    @Test
    fun `temporary BucketType should return temporary bucket name`() {
        val bucket: String? = factory.bucket(BucketType.RESTRICT_ACCESS)

        assertEquals(bucket, TEMPORARY_BUCKET)
    }

    @Test
    fun `temporary BucketType should return temporary client`() {
        val client: AmazonS3 = factory.client(BucketType.RESTRICT_ACCESS)
        val credentialsProvider = ReflectionTestUtils.getField(client, "awsCredentialsProvider") as AWSCredentialsProvider?
        val expectedProvider: AWSCredentialsProvider = AWSStaticCredentialsProvider(BasicAWSCredentials(TEMPORARY_KEY, TEMPORARY_SECRET))

        assertThat(client.region).isEqualTo(Region.SA_SaoPaulo)
        assertThat(credentialsProvider).isNotNull.usingRecursiveComparison().ignoringActualNullFields().isEqualTo(expectedProvider)
    }

    @Test
    fun `temporary BucketType should return temporary transfer manager`() {
        val transferManager: TransferManager = factory.transferManager(BucketType.RESTRICT_ACCESS)
        val client: AmazonS3 = factory.client(BucketType.RESTRICT_ACCESS)

        assertEquals(transferManager.amazonS3Client, client)
    }

    @Test
    fun `temporary transfer manager should not use default client`() {
        val transferManager: TransferManager = factory.transferManager(BucketType.RESTRICT_ACCESS)
        val client: AmazonS3 = factory.client(BucketType.FULL_ACCESS)

        assertNotEquals(transferManager.amazonS3Client, client)
    }

    @Test
    fun `default transfer manager should not use temporary client`() {
        val transferManager: TransferManager = factory.transferManager(BucketType.FULL_ACCESS)
        val client: AmazonS3 = factory.client(BucketType.RESTRICT_ACCESS)

        assertNotEquals(transferManager.amazonS3Client, client)
    }

    companion object {
        private const val DEFAULT_BUCKET = "cool-bucket-name"
        private const val DEFAULT_KEY = "fake-key"
        private const val DEFAULT_SECRET = "fake-secret"
        private val DEFAULT_REGION = Regions.US_EAST_1.getName()

        private const val TEMPORARY_BUCKET = "cool-temporary-bucket-name"
        private const val TEMPORARY_KEY = "fake-key-temporary"
        private const val TEMPORARY_SECRET = "fake-secret-temporary"
        private val TEMPORARY_REGION = Regions.SA_EAST_1.getName()
    }
}