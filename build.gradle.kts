plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("org.jetbrains.kotlin.kapt") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.1"
    id("io.micronaut.application") version "3.2.0"
}

version = "0.1-fixed"
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

    // local jars from ./gradlew build in micronaut-tracing
    // only tracing-jaeger was changed, tracing-core is a dependency
    implementation(files("tracing-core-4.0.3-SNAPSHOT.jar"))
    implementation(files("tracing-jaeger-4.0.3-SNAPSHOT.jar"))

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
