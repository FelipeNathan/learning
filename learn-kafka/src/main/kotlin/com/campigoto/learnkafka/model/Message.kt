package com.campigoto.learnkafka.model

import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "Felipe"
)