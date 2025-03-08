package com.avinash.Responses

data class Ping(
    val high: Double,
    val jitter: Double,
    val latency: Double,
    val low: Double
)