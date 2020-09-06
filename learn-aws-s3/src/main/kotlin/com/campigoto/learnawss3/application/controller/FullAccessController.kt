package com.campigoto.learnawss3.application.controller

import com.campigoto.learnawss3.domain.service.AwsS3Service
import com.campigoto.learnawss3.domain.valueObjects.AwsS3VO
import com.campigoto.learnawss3.domain.valueObjects.BucketType
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/full-access")
class FullAccessController(private val awsS3Service: AwsS3Service) {

    @PostMapping("/store", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun store(@RequestPart file: MultipartFile): String {

        if (file.isEmpty)
            return "File is required"

        this.awsS3Service.store(AwsS3VO(file.originalFilename, file.size, file.contentType, file.inputStream))

        return "File uploaded successfully"
    }

    @GetMapping("/buckets")
    fun buckets() = this.awsS3Service.listBuckets(BucketType.FULL_ACCESS)

    @GetMapping("/objects")
    fun listObjects() = this.awsS3Service.listObjects(BucketType.FULL_ACCESS)

    @GetMapping("/objects/{name}")
    fun getObject(@PathVariable("name") fileName: String) = this.awsS3Service.getObject(fileName, BucketType.FULL_ACCESS)

    @DeleteMapping("/objects/delete/{names}")
    fun deleteObjects(@PathVariable("names") names: Array<String>) = this.awsS3Service.delete(BucketType.FULL_ACCESS, *names)

}