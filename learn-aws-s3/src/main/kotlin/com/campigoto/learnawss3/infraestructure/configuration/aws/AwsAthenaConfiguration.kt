package com.campigoto.learnawss3.infraestructure.configuration.aws

import com.campigoto.learnawss3.domain.valueObjects.AwsAthenaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsAthenaConfiguration(private val awsClientBuilder: AwsClientBuilder) {

    @Bean
    fun client() = awsClientBuilder.athenaClient(properties())

    @Bean
    @ConfigurationProperties(prefix = "aws.athena")
    fun properties() = AwsAthenaProperties()
}