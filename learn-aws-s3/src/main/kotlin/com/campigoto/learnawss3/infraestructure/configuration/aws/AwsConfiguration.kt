package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.transfer.TransferManager
import com.campigoto.learnawss3.domain.valueObjects.AwsS3Properties
import com.campigoto.learnawss3.domain.valueObjects.BucketType

interface AwsConfiguration {

    fun getClient(): AmazonS3

    fun getTransferManager(): TransferManager

    fun getBucket(): String?

    fun getBucketType(): BucketType

    fun getProperties(): AwsS3Properties
}