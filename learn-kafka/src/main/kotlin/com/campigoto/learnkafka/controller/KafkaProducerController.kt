package com.campigoto.learnkafka.controller

import com.campigoto.learnkafka.service.KafkaProducerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class KafkaProducerController(
    private val kafkaProducer: KafkaProducerService
) {

    @GetMapping
    fun postMessage() {
        kafkaProducer.sendMessage()
    }

    @GetMapping("/list")
    fun postMessages() {
        kafkaProducer.sendMessages()
    }

    @GetMapping("/key/{key}")
    fun postMessageKey(@PathVariable("key") key: String) {
        kafkaProducer.sendMessageToKey(key)
    }
}