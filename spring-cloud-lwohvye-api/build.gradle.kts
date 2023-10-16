/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
}

description = "spring-cloud-lwohvye-api"
