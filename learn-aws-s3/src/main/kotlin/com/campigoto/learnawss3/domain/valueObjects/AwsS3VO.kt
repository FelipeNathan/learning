package com.campigoto.learnawss3.domain.valueObjects

import java.io.InputStream

data class AwsS3VO(
        var fileName: String? = null,
        var fileSize: Long? = null,
        var contentType: String? = null,
        var file: InputStream? = null,
        var bucketType: BucketType = BucketType.FULL_ACCESS)