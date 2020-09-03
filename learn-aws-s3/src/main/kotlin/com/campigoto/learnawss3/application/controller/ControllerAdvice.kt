package com.campigoto.learnawss3.application.controller

import com.amazonaws.AmazonServiceException
import com.amazonaws.SdkClientException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(SdkClientException::class)
    fun sdkException(exception: SdkClientException): ErrorResponse {

        val message = "Amazon S3 couldn't be contacted for a response, or the client\n" +
                "couldn't parse the response from Amazon S3."

        println(exception)

        return ErrorResponse(message)
    }

    @ExceptionHandler(AmazonServiceException::class)
    fun amazonServiceException(exception: AmazonServiceException): ErrorResponse {

        val message = "The call was transmitted successfully, but Amazon S3 couldn't process \n" +
                "it, so it returned an error response."

        println(exception)

        return ErrorResponse(message)
    }

    @ExceptionHandler(Exception::class)
    fun exception(exception: Exception): ErrorResponse {

        val message = "Not mapped error occurred"

        println(exception)

        return ErrorResponse(message)
    }

    class ErrorResponse(val message: String)
}