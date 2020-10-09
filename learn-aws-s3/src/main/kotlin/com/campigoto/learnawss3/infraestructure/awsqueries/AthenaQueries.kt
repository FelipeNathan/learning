package com.campigoto.learnawss3.infraestructure.awsqueries

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AthenaQueries(@Value("\${aws.athena.table-log}") private val table: String) {

    fun everythingQuery() = "SELECT * FROM $table"
    fun listByPutObjectEventQuery() = "SELECT * FROM $table WHERE eventName = 'PutObject'"
    fun listByGetObjectEventQuery() = "SELECT * FROM $table WHERE eventName = 'GetObject'"
}
