package com.campigoto.learnawss3.application

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.Region
import com.amazonaws.services.s3.transfer.TransferManager
import com.campigoto.learnawss3.application.controller.FullAccessController
import com.campigoto.learnawss3.application.controller.RestrictController
import com.campigoto.learnawss3.domain.valueObjects.BucketType
import com.campigoto.learnawss3.infraestructure.configuration.aws.AwsFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [LearnAwsS3Application::class])
@ActiveProfiles("test")
class LearnAwsS3ApplicationTests {

    @Autowired
    lateinit var fullAccessController: FullAccessController

    @Autowired
    lateinit var restrictController: RestrictController

    @Value("\${aws.s3.region}")
    lateinit var fullRegion: String

    @Value("\${aws.s3.restrict.region}")
    lateinit var restrictRegion: String

    @Autowired
    lateinit var awsFactory: AwsFactory

    lateinit var fullClient: AmazonS3
    lateinit var restrictClient: AmazonS3
    lateinit var fullTransferManager: TransferManager
    lateinit var restrictTransferManager: TransferManager

    @BeforeEach
    fun setup() {

        fullClient = awsFactory.client(BucketType.FULL_ACCESS)
        fullTransferManager = awsFactory.transferManager(BucketType.FULL_ACCESS)

        restrictClient = awsFactory.client(BucketType.RESTRICT_ACCESS)
        restrictTransferManager = awsFactory.transferManager(BucketType.RESTRICT_ACCESS)

    }

    @Test
    fun contextLoads() {

        assertNotNull(fullAccessController)
        assertNotNull(restrictController)
        assertNotNull(fullClient)
        assertNotNull(fullTransferManager)
        assertNotNull(restrictClient)
        assertNotNull(restrictTransferManager)
        assertNotNull(awsFactory)
    }

    @Test
    fun `is full access client configuration`() {

        val fullRegionType = Region.fromValue(fullRegion)
        val fullClientFactory = awsFactory.client(BucketType.FULL_ACCESS)

        assertEquals(fullClient.region, fullRegionType)
        assertEquals(fullTransferManager.amazonS3Client.region, fullRegionType)
        assertEquals(fullClientFactory.region, fullRegionType)
    }

    @Test
    fun `is restrict access client configuration`() {

        val restrictRegionType = Region.fromValue(restrictRegion)
        val fullClientFactory = awsFactory.client(BucketType.RESTRICT_ACCESS)

        assertEquals(restrictClient.region, restrictRegionType)
        assertEquals(restrictTransferManager.amazonS3Client.region, restrictRegionType)
        assertEquals(fullClientFactory.region, restrictRegionType)
    }

    /**
     * This was captured in @Configuration vs @Component class with @Bean methods
     * when use one @Bean dependency into another as call method
     *
     *  * <pre class="code">
     *     &#064;Bean
     *     public AmazonS3 client() {
     *         return new AmazonS3(...);
     *     }
     *
     *     &#064;Bean
     *     public TransferManager rransferManager() {
     *         return TransferManagerBuilder.standard().withS3Client(client()).build();
     *     }
     * </pre>
     * */
    @Test
    fun `pure client should be same instance of transfer manager client attribute`() {

        assertEquals(restrictClient, restrictTransferManager.amazonS3Client)
        assertEquals(fullClient, fullTransferManager.amazonS3Client)
    }
}
