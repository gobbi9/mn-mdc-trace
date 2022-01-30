package com.example.main

import io.jaegertracing.internal.MDCScopeManager
import io.micronaut.context.annotation.Factory
import io.opentracing.ScopeManager
import jakarta.inject.Singleton

@Factory
class ScopeManagerOverride {
    /*
    @Singleton
    fun noopScopeManager(): ScopeManager = NoopScopeManager.INSTANCE
     */

    @Singleton
    fun customMDCScopeManager(): ScopeManager = MDCScopeManager.Builder()
        .withMDCSampledKey("sentToJaeger").build()


}