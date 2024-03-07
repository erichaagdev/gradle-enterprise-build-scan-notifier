@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
  id("org.springframework.boot") version "3.2.3"
  kotlin("jvm") version "1.9.23"
  kotlin("kapt") version "1.9.23"
  kotlin("plugin.spring") version "1.9.23"
}

group = "dev.erichaag"
version = "0.0.1"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
    vendor.set(JvmVendorSpec.ADOPTIUM)
  }
}

repositories {
  mavenCentral()
}

val springBootVersion = SpringBootPlugin.BOM_COORDINATES.substringAfterLast(":")

dependencies {
  implementation(platform(SpringBootPlugin.BOM_COORDINATES))

  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:$springBootVersion")
  kapt("org.springframework.boot:spring-boot-configuration-processor:$springBootVersion") {
    because("https://github.com/spring-projects/spring-boot/issues/28046")
  }

  implementation(projects.gradleEnterpriseApiModels)
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.apache.commons:commons-text:1.11.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  testImplementation("org.springframework.boot:spring-boot-starter-test")

  implementation(group = "io.netty", name = "netty-resolver-dns-native-macos", classifier = "osx-aarch_64") {
    because("https://github.com/netty/netty/issues/11020")
  }
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
  }
}

testing.suites.withType<JvmTestSuite>().configureEach {
  useJUnitJupiter()
}
