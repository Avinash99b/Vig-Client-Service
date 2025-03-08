package com.avinash.Responses

data class Download(
    val bandwidth: Int,
    val bytes: Int,
    val elapsed: Int,
    val latency: Latency
)