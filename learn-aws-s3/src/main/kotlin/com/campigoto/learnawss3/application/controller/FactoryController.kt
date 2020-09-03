package com.campigoto.learnawss3.application.controller

import com.amazonaws.services.s3.model.ObjectListing
import com.campigoto.learnawss3.domain.valueObjects.BucketType
import com.campigoto.learnawss3.infraestructure.configuration.aws.AwsFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/factory")
class FactoryController(val awsFactory: AwsFactory) {

    @GetMapping("/objects/{type}")
    fun listObjects(@PathVariable("type") type: BucketType): ObjectListing? {

        val client = awsFactory.client(type)
        val bucket = awsFactory.bucket(type)
        return client.listObjects(bucket)
    }
}