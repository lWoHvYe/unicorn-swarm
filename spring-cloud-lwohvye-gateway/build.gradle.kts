/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("com.lwohvye.springcloud.java-conventions")
    id("io.freefair.lombok") version "8.3"
}

dependencies {
    api("org.springframework.cloud:spring-cloud-starter-gateway:4.0.7")
    api("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.0.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
}

description = "spring-cloud-lwohvye-gateway"
