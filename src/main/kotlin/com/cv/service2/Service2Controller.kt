package com.cv.service2

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
        value = ["/cv2"],
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE])
class Service2Controller {

    @GetMapping(
            value = ["/status/check"],
            produces = [MediaType.TEXT_PLAIN_VALUE],
            consumes = [MediaType.ALL_VALUE])
    fun status(): String {
        return "ok"
    }
}