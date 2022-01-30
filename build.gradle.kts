plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("org.jetbrains.kotlin.kapt") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.1"
    id("io.micronaut.application") version "3.2.0"
}

version = "0.1"
group = "com.example"

repositories {
    mavenCentral()
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-runtime")
    implementation("jakarta.annotation:jakarta.annotation-api")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut:micronaut-validation")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.20")

    // why am I hardcoding the versions? see the issue
    // https://github.com/micronaut-projects/micronaut-tracing/issues/19
    implementation("io.micronaut:micronaut-tracing:3.2.7")

    // zipkin
    runtimeOnly("io.zipkin.brave:brave-instrumentation-http:5.13.7")
    runtimeOnly("io.zipkin.reporter2:zipkin-reporter:2.16.3")
    implementation("io.opentracing.brave:brave-opentracing:1.0.0")

    // jaeger
    implementation("io.jaegertracing:jaeger-thrift:1.8.0")

}


application {
    mainClass.set("com.example.Application")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
    targetCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}
graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("kotest")
    processing {
        incremental(true)
        annotations("com.example.*")
    }
}

// used docker push after ./gradlew dockerBuild since ./gradlew dockerPush did not work
// https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/#_docker_support
tasks.named<com.bmuschko.gradle.docker.tasks.image.DockerBuildImage>("dockerBuild") {
    images.add("gobbi9/mn-mdc-trace:$version")
}
