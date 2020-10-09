package com.campigoto.learnawss3.infraestructure.awsqueries

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AthenaQueries(@Value("\${aws.athena.table-log}") private val table: String) {

    fun everythingQuery() = "SELECT * FROM $table"

    fun listByPutObjectEventQuery() = "SELECT * FROM $table WHERE eventName = 'PutObject'"

    fun listByGetObjectEventQuery() = "SELECT * FROM $table WHERE eventName = 'GetObject'"

    fun listNameAndSizeCreatedObjets() = """
            | SELECT json_extract(requestParameters, '$.key') as key,
            | json_extract(additionalEventData, '$.bytesTransferredIn') as byteTransferred
            | FROM $table
            | WHERE eventName = 'PutObject'""".trimMargin()

    fun listNameAndSizeReadObjets() = """
            | SELECT json_extract(requestParameters, '$.key') as key,
            | json_extract(additionalEventData, '$.bytesTransferredOut') as byteTransferred
            | FROM $table
            | WHERE eventName = 'GetObject'""".trimMargin()
}
