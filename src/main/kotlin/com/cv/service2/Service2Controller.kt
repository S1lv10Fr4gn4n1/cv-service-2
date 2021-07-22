package com.cv.service2

import com.cv.service2.message.MessageQueueSender
import com.cv.service2.service.ObjectDetectionService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.nio.file.Files
import java.nio.file.Path
import java.util.*


@RestController
@RequestMapping(
    value = ["/cv2"],
    produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
)
class Service2Controller(
    private val messageQueueSender: MessageQueueSender,
    private val objectDetectionService: ObjectDetectionService
) {

    private val uuid = UUID.randomUUID().toString()
    private val logger = LoggerFactory.getLogger(Service2Controller::class.java)

    @GetMapping(
        value = ["/healthcheck"],
        produces = [MediaType.TEXT_PLAIN_VALUE],
        consumes = [MediaType.ALL_VALUE]
    )
    fun healthcheck(): String {
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
        messageQueueSender.sendMessage(message)
        return message
    }

    @RequestMapping(
        value = ["/object-detection"],
        method = [RequestMethod.POST]
        )
    fun objectDetection() {
        // TODO SFS
        //  - upload image using postman/curl'
        //  - receive image
        //  - save image local
        //  - upload to S3
        //  - object detection predict
        //  - save to persistent storage (for now memory)
        //     - id, original image s3, output image, timestamp
        //  - response output image, id
        //  - verify how long it does take, if it's over 500ms, assycronos approach most take place (NEXT STEP?)

        val uuid = UUID.randomUUID().toString()
        val imageName = "elephant-car-scratch.jpg"
        val folderPath = "src/main/resources/images"
        val imagePath = "$folderPath/$uuid-$imageName"

        Files.copy(Path.of("$folderPath/$imageName"), Path.of(imagePath))

        logger.info(">>> objectDetection start $uuid")
        objectDetectionService.predict(imagePath)
//        messageQueueSender.sendMessage(message)
        logger.info(">>> objectDetection end $uuid")
    }


    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    fun serveFile(@PathVariable filename: String?): ResponseEntity<Resource?>? {
        val file: Resource = storageService.loadAsResource(filename)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename().toString() + "\"")
            .body<Resource?>(file)
    }

    @PostMapping("/")
    fun handleFileUpload(
        @RequestParam("file") file: MultipartFile,
        redirectAttributes: RedirectAttributes
    ): String? {
        storageService.store(file)
        redirectAttributes.addFlashAttribute(
            "message",
            "You successfully uploaded " + file.originalFilename + "!"
        )
        return "redirect:/"
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleStorageFileNotFound(exc: StorageFileNotFoundException?): ResponseEntity<*>? {
        return ResponseEntity.notFound().build<Any>()
    }

}