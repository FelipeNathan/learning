package com.campigoto.learnawss3.domain.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.*
import com.amazonaws.services.s3.transfer.TransferManager
import com.campigoto.learnawss3.application.exception.AwsObjectException
import com.campigoto.learnawss3.domain.valueObjects.AwsS3VO

abstract class AwsS3Service(
        private val bucket: String,
        private val client: AmazonS3,
        private val transferManager: TransferManager
) {

    fun store(vo: AwsS3VO) {

        validateAwsS3Vo(vo)

        val metadata = ObjectMetadata().apply {
            contentLength = vo.fileSize ?: 0
            contentType = vo.contentType
        }

        val putObject = PutObjectRequest(bucket, vo.fileName, vo.file, metadata)

        putObject.setGeneralProgressListener {
            println("Transfered bytes: ${it.bytesTransferred}")
        }

        val upload = this.transferManager.upload(putObject)
        upload.waitForCompletion()

        println("Transferation completed")
    }

    fun listBuckets(): MutableList<Bucket> = this.client.listBuckets()

    /**
     * @param key: the name of the object stored in Aws S3
     * */
    fun getObject(key: String): S3Object {

        val s3Object = this.client.getObject(GetObjectRequest(bucket, key))

        println("Content-Type ${s3Object.objectMetadata.contentType}")

        return s3Object
    }

    fun listObjects() {

        //Retorna de 2 em 2 buckets
        val request = ListObjectsV2Request().withBucketName(bucket).withMaxKeys(2)

        var result: ListObjectsV2Result?

        do {
            result = this.client.listObjectsV2(request)

            for (summary in result.objectSummaries) {
                println("Summary key ${summary.key} ~ size ${summary.size}")
            }

            //Se houver mais resultados, usa o 'próximo token' para os resultados a mais
            result.nextContinuationToken = result.continuationToken

        } while (result?.isTruncated!!)
    }

    /**
     * @param keys: the names of the objects to be deleted in Aws S3
     * */
    fun delete(vararg keys: String) {

        val keyVersions = keys.map { keyName ->
            DeleteObjectsRequest.KeyVersion(keyName)
        }

        val request = DeleteObjectsRequest(bucket).withKeys(keyVersions)

        val successfulDeletes = this.client.deleteObjects(request).deletedObjects.size

        println("Successfull deletes: $successfulDeletes")
    }

    private fun validateAwsS3Vo(vo: AwsS3VO) {

        if (vo.file == null)
            throw AwsObjectException("File is required")

        if (vo.fileName == null)
            throw AwsObjectException("File name is required")

        if (vo.contentType == null)
            throw AwsObjectException("Content type is required")
    }

}