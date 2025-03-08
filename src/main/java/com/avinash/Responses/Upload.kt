package com.avinash.Responses

data class Upload(
    val bandwidth: Int,
    val bytes: Int,
    val elapsed: Int,
    val latency: Latency
)