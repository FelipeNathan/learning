package com.campigoto.learnawss3.domain.valueObjects

import java.io.InputStream

class AwsS3VO(
        val fileName: String? = null,
        val fileSize: Long? = null,
        val contentType: String? = null,
        val file: InputStream? = null)