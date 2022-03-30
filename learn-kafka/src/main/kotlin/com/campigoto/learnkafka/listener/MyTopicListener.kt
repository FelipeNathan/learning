package com.campigoto.learnkafka.listener

import com.campigoto.learnkafka.fromJson
import com.campigoto.learnkafka.service.KafkaProducerService
import com.campigoto.learnkafka.service.Message
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener

@Configuration
class MyTopicListener {

    @KafkaListener(topics = [KafkaProducerService.TOPIC])
    fun listen(message: String) {
        val mapped = message.fromJson<Message>()
        println(mapped)
    }

    @KafkaListener(topics = [KafkaProducerService.TOPIC_LIST])
    fun listenList(message: String) {
        val mapped = message.fromJson<List<Message>>()
        println(mapped)
    }
}