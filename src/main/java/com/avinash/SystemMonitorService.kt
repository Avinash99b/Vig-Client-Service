package com.avinash

import com.avinash.Logger
import com.avinash.Responses.NormResponse
import com.avinash.SpeedTester
import com.avinash.Utils
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path
import java.io.File
import java.net.InetAddress

data class SystemInfo(
    val systemId: String,
    val storage: Long,
    val ram: Int,
    val processor: String,
    val upload_speed: Double,
    val download_speed: Double,
    val ping: Double,
    val serial_no: String
)

interface ApiService {
    @PATCH("systems/{id}")
    fun sendSystemData(@Path("id") systemId: String, @Body info: SystemInfo): Call<NormResponse>
}

class SystemMonitorService {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SystemMonitorService().startMonitoring()
        }
    }

    private val CONFIG_PATH: String = "C:\\ProgramData\\Avinash-Vignan-Info\\config.txt"

    private var systemId = "1"
    private var serverUrl = "http://sought-jennet-nearly.ngrok-free.app/"

    private val apiService: ApiService

    init {
        loadConfig()

        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    private fun loadConfig() {
        val file = File(CONFIG_PATH)
        if (file.exists()) {
            val lines = file.readLines()
            if (lines.size >= 2) {
                systemId = lines[0]
                serverUrl = lines[1]
            }
        } else {
            println("Config file not found")
            Utils.verifyFileExistence(file)
            file.writeText("$systemId\n$serverUrl")
        }
    }

    fun startMonitoring() {
        println("Starting Monitoring Service")
        while (true) {
            loadConfig()

            Logger.appendLog("Starting Speed Test at ${System.currentTimeMillis()}")
            val speedTestResult = SpeedTester.runTest()
            Logger.appendLog("Speed Test Completed at ${System.currentTimeMillis()}")

            println(speedTestResult.reportUrl)

            if (!speedTestResult.isSuccess) {
                Logger.appendErrorLog(speedTestResult.errorInfo)
                continue
            }
            val info = SystemInfo(
                systemId = systemId,
                storage = getDiskSize(),
                ram = getRAM(),
                processor = getProcessor(),
                upload_speed = speedTestResult.uploadSpeed,
                download_speed = speedTestResult.downloadSpeed,
                ping = speedTestResult.ping,
                serial_no = getDiskSerialNumber()
            )

            try {
                apiService.sendSystemData(systemId, info).enqueue(object : retrofit2.Callback<NormResponse> {
                    override fun onResponse(call: Call<NormResponse>, response: retrofit2.Response<NormResponse>) {
                        if (!response.isSuccessful) {
                            Logger.appendErrorLog("Failed to send data: $info, Error: ${response.message()}")
                            println("Failed to send data: $info")
                            return
                        }
                        println("Data sent successfully: $info")
                        Logger.appendLog("Data sent successfully: $info")
                    }

                    override fun onFailure(call: Call<NormResponse>, t: Throwable) {
                        t.printStackTrace()
                        Logger.appendErrorLog("Failed to send data: $info, Error: ${t.message}")
                        println("Failed to send data: $info")
                    }
                })


            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to send data: $info")
                Logger.appendErrorLog("Failed to send data: $info, Error: ${e.message}")
            }
            Thread.sleep(1000 * 10) // Sleep for 1 minute before next update
        }
    }


    private fun getDiskSerialNumber(): String {
        return executePowerShellCommand("Get-WmiObject Win32_PhysicalMedia | Select-Object -ExpandProperty SerialNumber")
    }

    private fun getDiskSize(): Long {
        return try {
            val result = executePowerShellCommand("""(Get-WmiObject Win32_DiskDrive | Select-Object -ExpandProperty Size | Measure-Object -Sum).Sum""")
            val sizeInBytes = result.trim().toLongOrNull() ?: return 0
            (sizeInBytes / (1024 * 1024 * 1024))
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private fun getRAM(): Int {
        return try {
            val result =
                executePowerShellCommand("Get-WmiObject Win32_PhysicalMemory | Measure-Object -Property Capacity -Sum | Select-Object -ExpandProperty Sum")
            val totalRam = result.trim().toLongOrNull() ?: return 0
            (totalRam / (1024 * 1024 * 1024)).toInt()
        } catch (e: Exception) {
            0
        }
    }

    private fun getProcessor(): String {
        return executePowerShellCommand("Get-WmiObject Win32_Processor | Select-Object -ExpandProperty Name")
    }

    private fun executePowerShellCommand(command: String): String {
        return try {
            val process = ProcessBuilder("powershell.exe", "-Command", command)
                .redirectErrorStream(true)
                .start()
            val reader = process.inputStream.bufferedReader()
            val output = reader.readText().trim()
            reader.close()
            process.waitFor()
            output
        } catch (e: Exception) {
            "Error"
        }
    }
}



