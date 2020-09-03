package com.campigoto.learnawss3.application.controller

import com.campigoto.learnawss3.domain.service.AwsS3RestrictAccessService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/restrict")
class RestrictController(val awsS3RestrictAccessService: AwsS3RestrictAccessService) : BaseController(awsS3RestrictAccessService)