package com.cv.service2.message

import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.core.MessageDeliveryMode
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class MessageQueueSender {

    @Autowired
    private lateinit var template: RabbitTemplate

    @Value("\${rabbitmq.queue.name}")
    private lateinit var queueName: String

    fun sendMessage(message: String) {
        template.convertAndSend(queueName, MessageBuilder
            .withBody(message.toByteArray())
            .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
            .build()
        )
    }
}