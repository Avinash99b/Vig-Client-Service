package com.avinash.Responses

data class OoklaSpeedTestResult(
    val download: Download,
    val `interface`: Interface,
    val isp: String,
    val packetLoss: Int,
    val ping: Ping,
    val result: Result,
    val server: Server,
    val timestamp: String,
    val type: String,
    val upload: Upload
)