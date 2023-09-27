/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("com.lwohvye.springcloud.java-conventions")
    id("org.springframework.boot") version "3.2.0-M2"
    id("io.spring.dependency-management") version "1.1.3"
    id("io.freefair.lombok") version "8.3"
}

extra["springCloudVersion"] = "2023.0.0-M1"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-websocket")
    api("org.springframework.cloud:spring-cloud-starter-config")
    api("com.alibaba:fastjson:1.2.83")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

description = "spring-cloud-lwohvye-weksocket"