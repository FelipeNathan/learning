package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.transfer.TransferManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsRestrictAccessConfiguration(
        @Value("\${aws.s3.restrict.region}") private val region: String,
        @Value("\${aws.s3.restrict.key}") private val key: String,
        @Value("\${aws.s3.restrict.secret}") private val secret: String,
        @Value("\${aws.s3.restrict.bucket}") private val bucket: String,
        private val awsClientBuilder: AwsClientBuilder
) : AwsConfiguration {

    @Bean("awsRestrictAccessClient")
    override fun getClient(): AmazonS3 {
        return awsClientBuilder.client(key, secret, region)
    }

    @Bean("awsRestrictAccessTransferManager")
    override fun getTransferManager(): TransferManager {
        return awsClientBuilder.transferManager(getClient())
    }

    @Bean("awsRestrictAccessBucket")
    override fun getBucket(): String = bucket
}