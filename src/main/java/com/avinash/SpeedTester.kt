package com.avinash

import com.avinash.Responses.OoklaSpeedTestResult
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class SpeedTester {
    data class SpeedTestResult(
        val isSuccess: Boolean = false,
        val errorInfo: String = "",
        val downloadSpeed: Double = 0.0,
        val uploadSpeed: Double = 0.0,
        val ping: Double = 0.0,
        val reportUrl: String = ""
    )

    companion object {
        @JvmStatic
        val gson = Gson()

        @JvmStatic
        fun runTest(): SpeedTestResult {
            try {
                val builder = ProcessBuilder("speedtest", "-f", "json-pretty")
                val process = builder.start()
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                val jsonResult = StringBuilder()
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    jsonResult.append(line)
                }
                val exitVal = process.waitFor()

                if (exitVal == 0) {
                    val speedTestResult = gson.fromJson(jsonResult.toString(), OoklaSpeedTestResult::class.java)

                    return SpeedTestResult(
                        true,
                        "None",
                        speedTestResult.download.bandwidth / 125000.toDouble(),
                        speedTestResult.upload.bandwidth / 125000.toDouble(),
                        speedTestResult.ping.latency,
                        speedTestResult.result.url
                    )
                } else {
                    println("Speedtest failed with exit code $exitVal")
                    return SpeedTestResult(false, "Speedtest failed with exit code $exitVal")
                }
            } catch (e: Exception) {
                e.printStackTrace()

                return SpeedTestResult(false, e.message.toString())
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            runTest()
        }
    }
}
