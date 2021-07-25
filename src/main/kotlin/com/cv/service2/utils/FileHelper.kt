package com.cv.service2.utils

import org.springframework.stereotype.Component
import java.io.File

@Component
class FileHelper {

    fun getOutputImagePath(inputImagePath: String): String {
        val file = File(inputImagePath)
        val fileExtension = file.name.split(".").last()
        val fileNameWithoutExtension = file.name.removeSuffix(".$fileExtension")
        return "${file.parent}/$fileNameWithoutExtension-output.$fileExtension"
    }
}