package com.campigoto.learnawss3.application.exception

class AwsAthenaException : Exception {

    constructor(message: String) : super(message)
    constructor(message: String, exception: Exception) : super(message, exception)
}