package com.avinash.Responses

data class Interface(
    val externalIp: String,
    val internalIp: String,
    val isVpn: Boolean,
    val macAddr: String,
    val name: String
)