package com.example.main

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import mu.KotlinLogging
import org.slf4j.MDC

private val logger = KotlinLogging.logger {}

@Controller("/test")
class TestController(
    private val testService: TestService
) {

    @Get("/{number}")
    fun issue(@PathVariable number: Int): String {
        MDC.put("test", "test value")
        logger.info {"controller test # $number!"}
        testService.testService(number)
        return "test # $number!"
    }
}