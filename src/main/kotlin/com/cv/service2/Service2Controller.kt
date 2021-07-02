package com.cv.service2

import com.cv.service2.message.MessageQueueSender
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(
    value = ["/cv2"],
    produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
)
class Service2Controller(private val messageQueueSender: MessageQueueSender) {

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
        messageQueueSender.sendMessage(message)
        return message
    }

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
}