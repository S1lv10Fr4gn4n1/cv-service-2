package com.cv.service2.service

import com.cv.service2.cv.ObjectDetection
import com.cv.service2.service.aws.s3.AwsS3ImageDownloader
import com.cv.service2.service.aws.s3.AwsS3ImageUploader
import org.springframework.stereotype.Service

@Service
class ObjectDetectionServiceImpl(
    private val objectDetection: ObjectDetection,
    private val s3ImageDownloader: AwsS3ImageDownloader,
    private val s3ImageUploader: AwsS3ImageUploader
) : ObjectDetectionService {

    override fun predict(imagePath: String): String {
        // TODO SFS
        //  - upload image to S3
        //  - objectDetection.predict(imagePath)
        //  - upload output image to S3
        //  - result S3 url?
        return objectDetection.predict(imagePath)
    }
}