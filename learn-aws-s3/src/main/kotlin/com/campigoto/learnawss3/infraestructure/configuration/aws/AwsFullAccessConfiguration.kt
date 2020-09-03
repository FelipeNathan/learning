package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.transfer.TransferManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class AwsFullAccessConfiguration(
        @Value("\${aws.s3.region}") override val region: String,
        @Value("\${aws.s3.key}") override val key: String,
        @Value("\${aws.s3.secret}") override val secret: String
) : AwsClientConfiguration(region, key, secret) {

    @Primary
    @Bean(name = ["fullAccessClient"])
    override fun client(): AmazonS3 {
        return super.client()
    }

    @Primary
    @Bean(name = ["fullAccessTransferManager"])
    override fun transferManager(client: AmazonS3): TransferManager {
        return super.transferManager(client)
    }
}