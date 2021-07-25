package com.cv.service2.service.aws.s3

interface ImageUploaderService {

    fun upload(filePath: String)
}