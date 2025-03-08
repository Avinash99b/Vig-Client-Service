package com.avinash

import java.io.File

class Logger {
    companion object{
        @JvmStatic
        private val ERROR_LOG_PATH: String = "C:\\ProgramData\\Avinash-Vignan-Info\\error_log.txt"

        @JvmStatic
        private val LOG_PATH: String = "C:\\ProgramData\\Avinash-Vignan-Info\\log.txt"


        @JvmStatic
        fun appendErrorLog(message: String) {
            val errorLogFile = File(ERROR_LOG_PATH)
            Utils.verifyFileExistence(errorLogFile)
            errorLogFile.appendText("$message\n")
        }

        fun appendLog(message: String) {
            val logFile = File(LOG_PATH)
            Utils.verifyFileExistence(logFile)
            logFile.appendText("$message\n")
        }
    }
}
