plugins {
    //主要是定义了这个，定义了SpringBoot的Version相关，并提供了application, bootJar, bootBuildImage这些Task
    alias(libs.plugins.spring.boot) apply false
    // This plugin simplifies the use of Lombok in Gradle
    id("io.freefair.lombok") version "8.4" apply false
}

extra["springCloudVersion"] = "2023.0.0-RC1"

subprojects {

    apply(plugin = "com.lwohvye.springcloud.java-conventions")
    apply(plugin = "io.freefair.lombok")

}
