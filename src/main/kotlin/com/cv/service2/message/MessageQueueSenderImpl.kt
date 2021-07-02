package com.cv.service2.message

import org.slf4j.LoggerFactory
import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.core.MessageDeliveryMode
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class MessageQueueSenderImpl(private val template: RabbitTemplate) : MessageQueueSender {

    private val logger = LoggerFactory.getLogger(MessageQueueSender::class.java)

    @Value("\${rabbitmq.queue.name}")
    private lateinit var queueName: String

    override fun sendMessage(message: String) {
        try {
            template.convertAndSend(
                queueName, MessageBuilder
                    .withBody(message.toByteArray())
                    .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                    .build()
            )
        } catch (e: Exception) {
            logger.error("cannot send to MQ", e)
        }
    }
}