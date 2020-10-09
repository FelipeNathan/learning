package com.campigoto.learnawss3.domain.mapper

import com.amazonaws.services.athena.model.ResultSet
import com.campigoto.learnawss3.domain.valueObjects.AwsAthenaObjectResult

object AwsAthenaMapper {

    fun resultSetToObjectResult(resultSet: ResultSet?): List<AwsAthenaObjectResult> {

        if (resultSet == null)
            return mutableListOf()

        val data = resultSet.rows.subList(1, resultSet.rows.size)

        return data.map { d -> AwsAthenaObjectResult(d.data[0].varCharValue, d.data[1].varCharValue) }
    }
}