package com.avinash

import java.io.File

class Utils {
    companion object{
        @JvmStatic
        fun verifyFileExistence(file: File){
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
                file.setReadable(true)
                file.setWritable(true)
                file.setExecutable(true)
            }
        }
    }
}