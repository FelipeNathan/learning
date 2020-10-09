package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.athena.AmazonAthena
import com.amazonaws.services.athena.AmazonAthenaClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import com.campigoto.learnawss3.domain.valueObjects.AwsAthenaProperties
import com.campigoto.learnawss3.domain.valueObjects.AwsS3Properties
import org.springframework.stereotype.Component

@Component
class AwsClientBuilder() {

    fun client(properties: AwsS3Properties): AmazonS3 {

        val credentials = getAwsStaticCredentialsProvider(properties.key, properties.secret)

        return AmazonS3ClientBuilder
                .standard()
                .withRegion(properties.region)
                .withCredentials(credentials)
                .build()
    }

    fun transferManager(client: AmazonS3): TransferManager {
        return TransferManagerBuilder.standard().withS3Client(client).build()
    }

    fun athenaClient(properties: AwsAthenaProperties): AmazonAthena {

        val credentials = getAwsStaticCredentialsProvider(properties.key, properties.secret)

        return AmazonAthenaClientBuilder.standard()
                .withRegion(properties.region)
                .withCredentials(credentials)
                .build()
    }

    private fun getAwsStaticCredentialsProvider(key: String?, secret: String?): AWSStaticCredentialsProvider {
        return AWSStaticCredentialsProvider(BasicAWSCredentials(key, secret))
    }
}