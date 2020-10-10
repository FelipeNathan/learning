package com.campigoto.learnawss3.domain.service

import com.amazonaws.services.athena.AmazonAthena
import com.amazonaws.services.athena.model.*
import com.campigoto.learnawss3.application.exception.AwsAthenaException
import com.campigoto.learnawss3.domain.mapper.AwsAthenaMapper
import com.campigoto.learnawss3.domain.valueObjects.AwsAthenaObjectResult
import com.campigoto.learnawss3.infraestructure.awsqueries.AthenaQueries
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AwsAthenaService(
        @Value("\${aws.athena.database}") private val database: String,
        private val athenaClient: AmazonAthena,
        private val athenaQueries: AthenaQueries
) {

    fun listGetObjectEvent(): ResultSet? {
        val executionQueryId = startQuery(athenaQueries.listByGetObjectEventQuery())
        return waitQuery(executionQueryId)
    }

    fun listPutObjectEvent(): ResultSet? {
        val executionQueryId = startQuery(athenaQueries.listByPutObjectEventQuery())
        return waitQuery(executionQueryId)
    }

    fun listEverything(): ResultSet? {
        val executionQueryId = startQuery(athenaQueries.everythingQuery())
        return waitQuery(executionQueryId)
    }

    fun listNameAndSizeCreatedObjets(): List<AwsAthenaObjectResult> {
        val executionQueryId = startQuery(athenaQueries.listNameAndSizeCreatedObjets())
        val resultSet = waitQuery(executionQueryId)

        return AwsAthenaMapper.resultSetToObjectResult(resultSet)
    }

    fun listNameAndSizeReadObjets(): List<AwsAthenaObjectResult> {
        val executionQueryId = startQuery(athenaQueries.listNameAndSizeReadObjets())
        val resultSet = waitQuery(executionQueryId)

        return AwsAthenaMapper.resultSetToObjectResult(resultSet)
    }

    private fun startQuery(query: String): String {

        try {
            val queryExecutionContext = QueryExecutionContext().withDatabase(database)

            val startExecutionContext = StartQueryExecutionRequest()
                    .withQueryExecutionContext(queryExecutionContext)
                    .withQueryString(query)
                    .withWorkGroup("learning-aws-athena")
                    .withResultConfiguration(ResultConfiguration().withOutputLocation("s3://felipe-nathan-athena-result"))

            return athenaClient.startQueryExecution(startExecutionContext).queryExecutionId
        } catch (e: Exception) {

            throw AwsAthenaException("Failed in startQuery", e)
        }
    }

    private fun waitQuery(queryExecutionId: String): ResultSet? {

        try {
            val runningQueryRequest = GetQueryExecutionRequest().withQueryExecutionId(queryExecutionId)
            var tries = 50
            while (tries-- > 0) {

                val runningQuery = athenaClient.getQueryExecution(runningQueryRequest)

                when (runningQuery.queryExecution.status.state) {
                    QueryExecutionState.FAILED.toString() -> throw AwsAthenaException("Query failed: ${runningQuery.queryExecution.status.stateChangeReason}")
                    QueryExecutionState.CANCELLED.toString() -> throw AwsAthenaException("Query canceled")
                    QueryExecutionState.SUCCEEDED.toString() -> {

                        val resultRequest = GetQueryResultsRequest().withQueryExecutionId(queryExecutionId)
                        val result = athenaClient.getQueryResults(resultRequest)

                        return result.resultSet
                    }
                    else -> Thread.sleep(WAIT_FOR)
                }
            }

            throw AwsAthenaException("Failed to get query execution status")
        } catch (e: Exception) {

            throw AwsAthenaException("Failed in waitQuery", e)
        }
    }

    companion object {
        const val WAIT_FOR = 1000L
    }
}