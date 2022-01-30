package com.example.main

import io.jaegertracing.Configuration
import io.jaegertracing.internal.MDCScopeManager
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import io.micronaut.tracing.jaeger.JaegerConfiguration
import io.micronaut.tracing.jaeger.JaegerTracerFactory


@Factory
@Primary
@Requires(beans = [JaegerConfiguration::class])
class AddMDCTraceIdForJaeger(configuration: JaegerConfiguration?) : JaegerTracerFactory(configuration) {
    override fun customizeConfiguration(configuration: Configuration?) {
        val mdcScopeManager = MDCScopeManager.Builder().build()
        configuration?.tracerBuilder?.withScopeManager(mdcScopeManager)
    }
}