package com.campigoto.learnawss3.application.controller

import com.amazonaws.services.athena.model.ResultSet
import com.campigoto.learnawss3.domain.service.AwsAthenaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/athena")
class AthenaController(private val awsAthenaService: AwsAthenaService) {

    @GetMapping("/get-objects-event")
    fun getByPutObjectEvent(): ResponseEntity<ResultSet> {
        return ResponseEntity(awsAthenaService.listGetObjectEvent(), HttpStatus.OK)
    }

    @GetMapping("/put-objects-event")
    fun getByGetObjectEvent(): ResponseEntity<ResultSet> {
        return ResponseEntity(awsAthenaService.listPutObjectEvent(), HttpStatus.OK)
    }

    @GetMapping("/everything")
    fun getEverything(): ResponseEntity<ResultSet?> {
        return ResponseEntity(awsAthenaService.listEverything(), HttpStatus.OK)
    }
}