package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import org.springframework.stereotype.Component

@Component
class AwsClientBuilder() {

    fun client(key: String, secret: String, region: String): AmazonS3 {

        val credentials = AWSStaticCredentialsProvider(BasicAWSCredentials(key, secret))

        return AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(credentials)
                .build()
    }

    fun transferManager(client: AmazonS3): TransferManager {
        return TransferManagerBuilder.standard().withS3Client(client).build()
    }
}