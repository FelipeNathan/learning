package com.campigoto.learnkafka.config

import com.campigoto.learnkafka.service.KafkaProducerService
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaConfig {
    @Bean
    fun topic() = NewTopic(KafkaProducerService.TOPIC, 2, 1)

    @Bean
    fun topicList() = NewTopic(KafkaProducerService.TOPIC_LIST, 2, 1)
}