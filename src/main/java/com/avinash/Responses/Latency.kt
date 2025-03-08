package com.avinash.Responses

data class Latency(
    val high: Double,
    val iqm: Double,
    val jitter: Double,
    val low: Double
)