package com.campigoto.learnawss3.domain.mapper

import com.amazonaws.services.athena.model.ResultSet
import com.campigoto.learnawss3.domain.valueObjects.AwsAthenaObjectResult

object AwsAthenaMapper {

    fun resultSetToObjectResult(resultSet: ResultSet?): List<AwsAthenaObjectResult> {

        if (resultSet == null)
            return mutableListOf()

        return resultSet.rows.subList(1, resultSet.rows.size).map { result ->

            var key = result.data[0].varCharValue
            val value = result.data[1].varCharValue

            val regexQuotes = "^\"(.*)\"$".toRegex()

            key = key.replace(regexQuotes, "$1")

            AwsAthenaObjectResult(key, value)

        }.toCollection(mutableListOf())
    }
}