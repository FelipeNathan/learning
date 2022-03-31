package com.campigoto.learnkafka.service

import com.campigoto.learnkafka.model.Message
import com.campigoto.learnkafka.toJson
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducerService(
    private val producer: KafkaTemplate<String, Any>
) {

    fun sendMessage() {
        producer.send(TOPIC, Message().toJson())
    }

    fun sendMessages() {
        producer.send(TOPIC_LIST, listOf(Message(), Message()).toJson())
    }

    fun sendMessageToKey(key: String) {
        producer.send(TOPIC, key, Message().toJson())
    }

    companion object {
        const val TOPIC = "my-topic"
        const val TOPIC_LIST = "my-topic-list"
    }
}
