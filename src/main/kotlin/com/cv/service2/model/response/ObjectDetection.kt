package com.cv.service2.model.response

sealed class ObjectDetection {

    class Success(val id: String, val message: String) : ObjectDetection()
    class Error(val message: String) : ObjectDetection()
}