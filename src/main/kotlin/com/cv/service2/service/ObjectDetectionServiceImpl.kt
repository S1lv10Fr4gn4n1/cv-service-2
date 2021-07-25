package com.cv.service2.service

import com.cv.service2.cv.ObjectDetection
import com.cv.service2.service.aws.s3.ImageUploaderService
import com.cv.service2.service.model.ObjectDetectionResult
import com.cv.service2.utils.FileHelper
import org.springframework.stereotype.Service
import java.util.*

@Service
class ObjectDetectionServiceImpl(
    private val fileHelper: FileHelper,
    private val imageUploaderService: ImageUploaderService,
    private val objectDetection: ObjectDetection
) : ObjectDetectionService {

    override fun predict(inputImagePath: String): ObjectDetectionResult {
        // TODO SFS
        //  - upload to S3
        //  - object detection predict
        //  - save to persistent storage (for now memory)
        //     - id, original image s3, output image, timestamp
        //  - upload output image to S3
        //  - response output image, id, S3 url?
        //  - verify how long it does take, if it's over 500ms, assycronos approach most take place (NEXT STEP?)

        val outImagePath = fileHelper.getOutputImagePath(inputImagePath)
        objectDetection.predict(inputImagePath, outImagePath)

        // upload original image
        imageUploaderService.upload(inputImagePath)

        // upload output image
        imageUploaderService.upload(outImagePath)

//         TODO SFS
//        messageQueueSender.sendMessage(message)
        return ObjectDetectionResult.Success(
            id = UUID.randomUUID().toString(),
            message = "Success"
        )
    }
}