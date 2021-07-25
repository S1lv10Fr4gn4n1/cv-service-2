package com.cv.service2.service.aws.s3

interface ImageDownloaderService {

    fun download(filePath: String)
}