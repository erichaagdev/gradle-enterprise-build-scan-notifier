@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
  id("org.springframework.boot") version "3.0.3"
  kotlin("jvm") version "1.8.10"
  kotlin("plugin.spring") version "1.8.10"
}

group = "dev.erichaag"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
    vendor.set(JvmVendorSpec.ADOPTIUM)  }
}

dependencies {
  implementation(platform(SpringBootPlugin.BOM_COORDINATES))
  implementation("org.springframework.boot:spring-boot-starter")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
  }
}

testing.suites.withType<JvmTestSuite>().configureEach {
  useJUnitJupiter()
}
