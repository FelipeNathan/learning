package com.campigoto.learnawss3.application.controller

import com.amazonaws.services.athena.model.ResultSet
import com.amazonaws.services.athena.model.Row
import com.campigoto.learnawss3.domain.service.AwsAthenaService
import com.campigoto.learnawss3.domain.valueObjects.AwsAthenaObjectResult
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

    @GetMapping("/created-objects")
    fun listNameAndSizeCreatedObjets(): ResponseEntity<List<AwsAthenaObjectResult>> {
        return ResponseEntity(awsAthenaService.listNameAndSizeCreatedObjets(), HttpStatus.OK)
    }

    @GetMapping("/read-objects")
    fun listNameAndSizeReadObjets(): ResponseEntity<List<AwsAthenaObjectResult>> {
        return ResponseEntity(awsAthenaService.listNameAndSizeReadObjets(), HttpStatus.OK)
    }
}