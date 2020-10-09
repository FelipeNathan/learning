package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.campigoto.learnawss3.domain.valueObjects.BucketType
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class AwsFactory(private val awsConfigurations: List<AwsConfiguration>, private val athenaConfiguration: AwsAthenaConfiguration) {

    private val configurations = mutableMapOf<BucketType, AwsConfiguration>()

    @PostConstruct
    fun init() = awsConfigurations.forEach { config -> configurations[config.getBucketType()] = config }

    fun bucket(bucketType: BucketType) = configurations[bucketType]!!.getBucket()

    fun client(bucketType: BucketType) = configurations[bucketType]!!.getClient()

    fun transferManager(bucketType: BucketType) = configurations[bucketType]!!.getTransferManager()

    fun athenaClient() = athenaConfiguration.client()
}