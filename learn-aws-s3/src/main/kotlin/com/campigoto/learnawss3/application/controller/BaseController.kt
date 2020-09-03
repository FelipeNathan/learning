package com.campigoto.learnawss3.application.controller

import com.campigoto.learnawss3.domain.service.AwsS3Service
import com.campigoto.learnawss3.domain.valueObjects.AwsS3VO
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

abstract class BaseController(private val awsS3Service: AwsS3Service) {

    @PostMapping("/store", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun store(@RequestPart file: MultipartFile): String {

        if (file.isEmpty)
            return "File is required"

        this.awsS3Service.store(AwsS3VO(file.originalFilename, file.size, file.contentType, file.inputStream))

        return "File uploaded successfully"
    }

    @GetMapping("/buckets")
    fun buckets() = this.awsS3Service.listBuckets()

    @GetMapping("/objects")
    fun listObjects() = this.awsS3Service.listObjects()

    @GetMapping("/objects/{name}")
    fun getObject(@PathVariable("name") fileName: String) = this.awsS3Service.getObject(fileName)

    @DeleteMapping("/objects/delete/{names}")
    fun deleteObjects(@PathVariable("names") names: Array<String>) = this.awsS3Service.delete(*names)
}