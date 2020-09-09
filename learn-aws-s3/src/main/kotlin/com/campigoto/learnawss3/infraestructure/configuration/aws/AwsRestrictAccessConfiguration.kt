package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.transfer.TransferManager
import com.campigoto.learnawss3.domain.valueObjects.BucketType
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

    override fun getBucket(): String = bucket

    override fun getBucketType() = BucketType.RESTRICT_ACCESS
}