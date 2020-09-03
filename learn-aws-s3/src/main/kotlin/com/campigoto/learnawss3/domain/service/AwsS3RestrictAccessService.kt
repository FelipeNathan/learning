package com.campigoto.learnawss3.domain.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.transfer.TransferManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AwsS3RestrictAccessService (
        @Value("\${aws.s3.restrict.bucket}") private val bucket: String,
        @Qualifier("restrictAccessClient") private val client: AmazonS3,
        @Qualifier("restrictAccessTransferManager") private val transferManager: TransferManager
) : AwsS3Service (
        bucket,
        client,
        transferManager
)