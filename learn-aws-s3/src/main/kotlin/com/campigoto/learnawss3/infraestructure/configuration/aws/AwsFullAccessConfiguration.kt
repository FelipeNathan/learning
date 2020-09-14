package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.transfer.TransferManager
import com.campigoto.learnawss3.domain.valueObjects.AwsS3Properties
import com.campigoto.learnawss3.domain.valueObjects.BucketType
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsFullAccessConfiguration(private val awsClientBuilder: AwsClientBuilder) : AwsConfiguration {

    @Bean("awsFullAccessClient")
    override fun getClient(): AmazonS3 {
        return awsClientBuilder.client(getProperties())
    }

    @Bean("awsFullAccessTransferManager")
    override fun getTransferManager(): TransferManager {
        return awsClientBuilder.transferManager(getClient())
    }

    override fun getBucket(): String? = getProperties().bucket

    override fun getBucketType() = BucketType.FULL_ACCESS

    @Bean("awsFullAccessProperties")
    @ConfigurationProperties("aws.s3")
    override fun getProperties() = AwsS3Properties()
}