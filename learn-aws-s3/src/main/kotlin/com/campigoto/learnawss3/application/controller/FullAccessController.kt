package com.campigoto.learnawss3.application.controller

import com.campigoto.learnawss3.domain.service.AwsS3FullAccessService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/full-access")
class FullAccessController (awsS3FullAccessService: AwsS3FullAccessService) : BaseController(awsS3FullAccessService)