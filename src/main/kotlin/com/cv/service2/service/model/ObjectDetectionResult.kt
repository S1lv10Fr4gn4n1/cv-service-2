package com.cv.service2.service.model

sealed class ObjectDetectionResult {

    class Success(val id: String, val message: String) : ObjectDetectionResult()
    class Error(val message: String) : ObjectDetectionResult()

}