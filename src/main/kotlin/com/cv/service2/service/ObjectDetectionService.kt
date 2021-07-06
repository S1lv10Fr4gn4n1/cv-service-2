package com.cv.service2.service

interface ObjectDetectionService {

    fun predict(imagePath: String): String
}