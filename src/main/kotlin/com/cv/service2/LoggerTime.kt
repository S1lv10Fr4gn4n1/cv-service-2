package com.cv.service2

import org.slf4j.LoggerFactory

object LoggerTime {

    private val logger = LoggerFactory.getLogger(LoggerTime::class.java.simpleName)
    private val keyTimers = mutableMapOf<String, Long>()

    fun start(key: String) {
        keyTimers[key] = System.currentTimeMillis()
    }

    fun stop(key: String, extra: String = "") {
        val start = keyTimers.remove(key) ?: 0
        val stop = System.currentTimeMillis()
        val diff = stop - start
        logger.info("$key duration $diff ms $extra")
    }
}