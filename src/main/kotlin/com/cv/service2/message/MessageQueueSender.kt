package com.cv.service2.message

interface MessageQueueSender {

    fun sendMessage(message: String)
}