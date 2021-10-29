plugins {
    val kotlinVersion = "1.5.10"
    val miraiVersion = "2.7.1"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version miraiVersion
}

group = "vcg"
version = "1.1.2-BETA"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenLocal()
    mavenCentral()
}
