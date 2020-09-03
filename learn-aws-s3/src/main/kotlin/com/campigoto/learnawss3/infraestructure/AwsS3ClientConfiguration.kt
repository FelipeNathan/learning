package com.campigoto.learnawss3.infraestructure

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsS3ClientConfiguration(
        @Value("\${aws.s3.region}") val region: String,
        @Value("\${aws.s3.key}") val key: String,
        @Value("\${aws.s3.secret}") val secret: String
) {

    @Bean
    fun client(): AmazonS3 {

        val credentials = AWSStaticCredentialsProvider(BasicAWSCredentials(key, secret))

        return AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(credentials)
                .build()
    }

    @Bean
    fun transferManager(client: AmazonS3): TransferManager {
        return TransferManagerBuilder.standard().withS3Client(client).build()
    }
}