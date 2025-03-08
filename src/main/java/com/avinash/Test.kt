package com.avinash

class Test {
   companion object{
       @JvmStatic
       fun main(args: Array<String>) {
           println(getDiskSize())
       }

       private fun getDiskSize(): Int {
           return try {
               val result = executePowerShellCommand(
                   """(Get-WmiObject Win32_DiskDrive | Select-Object -ExpandProperty Size | Measure-Object -Sum).Sum""")

               val sizeInBytes = result.trim().toLongOrNull() ?: return 0
               return (sizeInBytes / (1024 * 1024 * 1024)).toInt()
           } catch (e: Exception) {
               e.printStackTrace()
               0
           }
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
}
