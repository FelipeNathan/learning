package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import com.campigoto.learnawss3.domain.valueObjects.BucketType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AwsFactory(
        @Value("\${aws.s3.restrict.region}") val restrictRegion: String,
        @Value("\${aws.s3.restrict.key}") val restrictKey: String,
        @Value("\${aws.s3.restrict.secret}") val restrictSecret: String,
        @Value("\${aws.s3.restrict.bucket}") val restrictBucket: String,
        @Value("\${aws.s3.region}") val fullRegion: String,
        @Value("\${aws.s3.key}") val fullKey: String,
        @Value("\${aws.s3.secret}") val fullSecret: String,
        @Value("\${aws.s3.bucket}") val fullBucket: String
) {

    fun bucket(bucketType: BucketType): String? {
        return when (bucketType) {
            BucketType.FULL_ACCESS -> fullBucket
            BucketType.RESTRICT_ACCESS -> restrictBucket
        }
    }

    fun client(bucketType: BucketType): AmazonS3 {

        var key = ""
        var secret = ""
        var region = ""

        when (bucketType) {
            BucketType.FULL_ACCESS -> {
                key = fullKey
                secret = fullSecret
                region = fullRegion
            }

            BucketType.RESTRICT_ACCESS -> {
                key = restrictKey
                secret = restrictSecret
                region = restrictRegion
            }
        }

        val credentials = AWSStaticCredentialsProvider(BasicAWSCredentials(key, secret))

        return AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(credentials)
                .build()
    }

    fun transferManager(bucketType: BucketType): TransferManager {
        return TransferManagerBuilder.standard().withS3Client(client(bucketType)).build()
    }
}