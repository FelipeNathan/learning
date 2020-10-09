package com.campigoto.learnawss3.domain.service

import com.amazonaws.services.athena.AmazonAthena
import com.amazonaws.services.athena.model.*
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

    private fun startQuery(query: String): String {

        val queryExecutionContext = QueryExecutionContext().withDatabase(database)

        val startExecutionContext = StartQueryExecutionRequest()
                .withQueryExecutionContext(queryExecutionContext)
                .withQueryString(query)
                .withResultConfiguration(ResultConfiguration().withOutputLocation("s3://felipe-nathan-athena-result"))

        return athenaClient.startQueryExecution(startExecutionContext).queryExecutionId
    }

    private fun waitQuery(queryExecutionId: String): ResultSet? {

        val runningQueryRequest = GetQueryExecutionRequest().withQueryExecutionId(queryExecutionId)

        while (true) {

            val runningQuery = athenaClient.getQueryExecution(runningQueryRequest)

            when (runningQuery.queryExecution.status.state) {
                QueryExecutionState.FAILED.toString() -> throw RuntimeException("Query failed: ${runningQuery.queryExecution.status.stateChangeReason}")
                QueryExecutionState.CANCELLED.toString() -> throw RuntimeException("Query canceled")
                QueryExecutionState.SUCCEEDED.toString() -> {

                    val resultRequest = GetQueryResultsRequest().withQueryExecutionId(queryExecutionId)
                    val result = athenaClient.getQueryResults(resultRequest)

                    return result.resultSet
                }
                else -> Thread.sleep(WAIT_FOR)
            }
        }
    }

    companion object {
        const val WAIT_FOR = 1000L
    }
}