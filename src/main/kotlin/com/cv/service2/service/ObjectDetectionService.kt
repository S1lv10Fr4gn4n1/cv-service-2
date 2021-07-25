package com.cv.service2.service

import com.cv.service2.service.model.ObjectDetectionResult

interface ObjectDetectionService {

    fun predict(inputImagePath: String): ObjectDetectionResult
}