package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.transfer.TransferManager

interface AwsConfiguration {

    fun getClient(): AmazonS3

    fun getTransferManager(): TransferManager

    fun getBucket(): String
}