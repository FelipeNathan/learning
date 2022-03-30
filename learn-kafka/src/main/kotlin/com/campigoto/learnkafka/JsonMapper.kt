package com.campigoto.learnkafka

import com.fasterxml.jackson.databind.ObjectMapper

inline fun <reified T> T.toJson(): String {
    return ObjectMapper().writeValueAsString(this)
}

inline fun <reified T> String.fromJson(): T {
    return ObjectMapper().readValue(this, T::class.java)
}
