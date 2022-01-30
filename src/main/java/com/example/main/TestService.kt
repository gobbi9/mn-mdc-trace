package com.example.main

import io.micronaut.tracing.annotation.NewSpan
import jakarta.inject.Singleton
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@Singleton
open class TestService {

    @NewSpan
    open fun testService(number: Int) {
        logger.info { "service test # $number!" }
    }
}