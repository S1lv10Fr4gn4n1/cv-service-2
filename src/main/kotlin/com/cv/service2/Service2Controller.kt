package com.cv.service2

import com.cv.service2.model.response.ObjectDetection
import com.cv.service2.service.ObjectDetectionService
import com.cv.service2.service.model.ObjectDetectionResult
import com.cv.service2.utils.MultipartFileHelper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/cv2")
class Service2Controller(
    private val multipartFileHelper: MultipartFileHelper,
    private val objectDetectionService: ObjectDetectionService
) {

    private val uuid = UUID.randomUUID().toString()
    private val logger = LoggerFactory.getLogger(Service2Controller::class.java)

    @GetMapping(
        value = ["/healthcheck"],
        produces = [MediaType.TEXT_PLAIN_VALUE],
        consumes = [MediaType.ALL_VALUE]
    )
    fun healthCheck(): String {
        val message = "healthcheck $uuid"
        logger.info(">> $message")
        return message
    }

    @Deprecated("Remove this method after tests")
    @RequestMapping(
        value = ["/helloworld"],
        method = [RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD],
        produces = [MediaType.TEXT_PLAIN_VALUE],
        consumes = [MediaType.ALL_VALUE]
    )
    fun helloworld(): String {
        val message = "helloworld $uuid"
        logger.info(">> $message")
        return message
    }

    @RequestMapping(
        value = ["/object-detection"],
        method = [RequestMethod.POST]
    )
    fun objectDetection(
        @RequestParam(
            value = "image",
            required = false
        ) image: MultipartFile
    ): ResponseEntity<ObjectDetection> {
        try {
            LoggerTime.start("object-detection")
            val tempLocalFile = multipartFileHelper.saveToLocalStorage(image)
            val result = objectDetectionService.predict(tempLocalFile)
            LoggerTime.stop("object-detection")

            return when (result) {
                is ObjectDetectionResult.Success ->
                    ResponseEntity
                        .ok()
                        .body(ObjectDetection.Success(result.id, result.message))
                is ObjectDetectionResult.Error ->
                    ResponseEntity
                        .ok()
                        .body(ObjectDetection.Error(result.message))
            }
        } catch (e: Exception) {
            logger.error("Failure", e)
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ObjectDetection.Error(e.message ?: "error"))
        }
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ObjectDetection.Error("Error"))
    }

    /**
     * This method it's used just for testing using Apache AB
     */
    @RequestMapping(
        value = ["/object-detection/base64"],
        method = [RequestMethod.POST]
    )
    fun objectDetectionBase64(
        @RequestParam(
            value = "image",
            required = false
        ) image: MultipartFile
    ): ResponseEntity<ObjectDetection> {
        try {
            LoggerTime.start("object-detection")
            val tempLocalFile = multipartFileHelper.saveToLocalStorageBase64(image)
            objectDetectionService.predict(tempLocalFile)
            LoggerTime.stop("object-detection")
        } catch (e: Exception) {
            logger.error("Failure", e)
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ObjectDetection.Error(e.message ?: "error"))
        }
        return ResponseEntity
            .ok()
            .body(
                ObjectDetection.Success(
                    id = UUID.randomUUID().toString(), // TODO SFS
                    message = "Image uploaded with success"
                )
            )
    }
}