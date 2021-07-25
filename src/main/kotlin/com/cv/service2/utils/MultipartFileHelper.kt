package com.cv.service2.utils

import com.cv.service2.LoggerTime
import com.cv.service2.service.storage.StorageService
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*
import kotlin.io.path.absolutePathString


@Component
class MultipartFileHelper(private val storageService: StorageService) {

    fun saveToLocalStorage(file: MultipartFile): String {
        LoggerTime.start("save-MultipartFile")
        if (file.isEmpty) {
            throw Exception("File not found")
        }

        val fileName = UUID.randomUUID().toString() + "_" + file.originalFilename
        val destinationFile = Paths.get(storageService.getTempDir(), fileName)
        file.inputStream.use {
            Files.copy(it, destinationFile, StandardCopyOption.REPLACE_EXISTING)
        }
        LoggerTime.stop("save-MultipartFile")
        return destinationFile.absolutePathString()
    }

    /**
     * This method is just for testing using Apache AB
     */
    fun saveToLocalStorageBase64(file: MultipartFile): String {
        LoggerTime.start("save-MultipartFile")
        if (file.isEmpty) {
            throw Exception("File not found")
        }

        val fileName = UUID.randomUUID().toString() + "_" + file.originalFilename
        val destinationFile = Paths.get(storageService.getTempDir(), fileName)

        file.inputStream.use {
            // TODO SFS, weird hack to make it work
            val byteArray = it.readAllBytes().dropLast(2).toByteArray()
            val decodedImage = Base64.getDecoder().decode(byteArray)
            Files.copy(ByteArrayInputStream(decodedImage), destinationFile, StandardCopyOption.REPLACE_EXISTING)
        }
        LoggerTime.stop("save-MultipartFile")
        return destinationFile.absolutePathString()
    }
}