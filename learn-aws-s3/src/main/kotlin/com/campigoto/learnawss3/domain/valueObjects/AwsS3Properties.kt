package com.campigoto.learnawss3.domain.valueObjects

data class AwsS3Properties(
        var region: String? = null,
        var bucket: String? = null,
        var secret: String? = null,
        var key: String? = null
)