package com.cv.service2.service.storage

interface StorageService {

    fun getTempDir(): String

    fun delete(filePath: String)

    fun deleteAll()
}