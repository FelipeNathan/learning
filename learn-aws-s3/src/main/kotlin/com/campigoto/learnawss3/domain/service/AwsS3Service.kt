package com.campigoto.learnawss3.domain.service

import com.amazonaws.services.s3.model.*
import com.campigoto.learnawss3.application.exception.AwsObjectException
import com.campigoto.learnawss3.domain.valueObjects.AwsS3VO
import com.campigoto.learnawss3.domain.valueObjects.BucketType
import com.campigoto.learnawss3.infraestructure.configuration.aws.AwsFactory
import org.springframework.stereotype.Service

@Service
class AwsS3Service(private val awsFactory: AwsFactory) {

    private fun getClient(bucketType: BucketType) = awsFactory.client(bucketType)

    private fun getTransferManager(bucketType: BucketType) = awsFactory.transferManager(bucketType)

    private fun getBucketName(bucketType: BucketType) = awsFactory.bucket(bucketType)

    fun store(vo: AwsS3VO) {

        validateAwsS3Vo(vo)

        val metadata = ObjectMetadata().apply {
            contentLength = vo.fileSize ?: 0
            contentType = vo.contentType
        }

        val putObject = PutObjectRequest(awsFactory.bucket(vo.bucketType), vo.fileName, vo.file, metadata)

        putObject.setGeneralProgressListener {
            println("Transfered bytes: ${it.bytesTransferred}")
        }

        val upload = getTransferManager(vo.bucketType).upload(putObject)
        upload.waitForCompletion()

        println("Transferation completed")
    }

    fun listBuckets(bucketType: BucketType): MutableList<Bucket> = getClient(bucketType).listBuckets()

    /**
     * @param key: the name of the object stored in Aws S3
     * */
    fun getObject(key: String, bucketType: BucketType): S3Object {

        val s3Object = getClient(bucketType).getObject(GetObjectRequest(getBucketName(bucketType), key))

        println("Content-Type ${s3Object.objectMetadata.contentType}")

        return s3Object
    }

    fun listObjects(bucketType: BucketType) {

        //Retorna de 2 em 2 buckets
        val request = ListObjectsV2Request().withBucketName(getBucketName(bucketType)).withMaxKeys(2)

        var result: ListObjectsV2Result?

        do {
            result = getClient(bucketType).listObjectsV2(request)

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
    fun delete(bucketType: BucketType, vararg keys: String) {

        val keyVersions = keys.map { keyName ->
            DeleteObjectsRequest.KeyVersion(keyName)
        }

        val request = DeleteObjectsRequest(getBucketName(bucketType)).withKeys(keyVersions)

        val successfulDeletes = getClient(bucketType).deleteObjects(request).deletedObjects.size

        println("Successfull deletes: $successfulDeletes")
    }

    private fun validateAwsS3Vo(vo: AwsS3VO) {

        if (vo.file == null)
            throw AwsObjectException("File is required")

        if (vo.fileName == null)
            throw AwsObjectException("File name is required")

        if (vo.contentType == null)
            throw AwsObjectException("Content type is required")

        val bucketName = awsFactory.bucket(vo.bucketType)
        if (bucketName.isEmpty())
            throw AwsObjectException("Bucket name is required")
    }

}