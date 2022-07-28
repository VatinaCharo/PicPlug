plugins {
    val kotlinVersion = "1.7.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.12.0"
}

group = "nju.eur3ka"
version = "1.2.1"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
