package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.transfer.TransferManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsRestrictAccessConfiguration(
        @Value("\${aws.s3.restrict.region}") override val region: String,
        @Value("\${aws.s3.restrict.key}") override val key: String,
        @Value("\${aws.s3.restrict.secret}") override val secret: String
) : AwsClientConfiguration(region, key, secret) {

    @Bean(name = ["restrictAccessClient"])
    override fun client(): AmazonS3 {
        return super.client()
    }

    @Bean(name = ["restrictAccessTransferManager"])
    override fun transferManager(@Qualifier("restrictAccessClient") client: AmazonS3): TransferManager {
        return super.transferManager(client)
    }
}