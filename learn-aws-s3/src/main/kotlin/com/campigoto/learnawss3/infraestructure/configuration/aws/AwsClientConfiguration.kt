package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.TransferManagerBuilder

abstract class AwsClientConfiguration(
        open val region: String? = null,
        open val key: String? = null,
        open val secret: String? = null) {

    open fun client(): AmazonS3 {

        val credentials = AWSStaticCredentialsProvider(BasicAWSCredentials(key, secret))

        return AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(credentials)
                .build()
    }

    open fun transferManager(): TransferManager {
        return TransferManagerBuilder.standard().withS3Client(client()).build()
    }
}