package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.transfer.TransferManager
import com.campigoto.learnawss3.domain.valueObjects.BucketType
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class AwsFactory(private val applicationContext: ApplicationContext) {

    fun bucket(bucketType: BucketType): String? {
        return when (bucketType) {
            BucketType.FULL_ACCESS -> applicationContext.getBean("awsFullAccessBucket")
            BucketType.RESTRICT_ACCESS -> applicationContext.getBean("awsRestrictAccessBucket")
        } as String?
    }

    fun client(bucketType: BucketType): AmazonS3 {
        return when (bucketType) {
            BucketType.FULL_ACCESS -> applicationContext.getBean("awsFullAccessClient")
            BucketType.RESTRICT_ACCESS -> applicationContext.getBean("awsRestrictAccessClient")
        } as AmazonS3
    }

    fun transferManager(bucketType: BucketType): TransferManager {
        return when (bucketType) {
            BucketType.FULL_ACCESS -> applicationContext.getBean("awsFullAccessTransferManager")
            BucketType.RESTRICT_ACCESS -> applicationContext.getBean("awsRestrictAccessTransferManager")
        } as TransferManager
    }
}