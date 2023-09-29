plugins {
    val kotlinVersion = "1.8.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.15.0"
}

group = "nju.eur3ka"
version = "2.1.1"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

mirai {
    jvmTarget = JavaVersion.VERSION_1_8
}
