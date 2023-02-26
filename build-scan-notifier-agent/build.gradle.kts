@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
  id("org.springframework.boot") version "3.0.3"
  kotlin("jvm") version "1.8.10"
  kotlin("kapt") version "1.8.10"
  kotlin("plugin.spring") version "1.8.10"
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

dependencies {
  implementation(platform(SpringBootPlugin.BOM_COORDINATES))

  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:3.0.3")
  kapt("org.springframework.boot:spring-boot-configuration-processor:3.0.3") {
    because("https://github.com/spring-projects/spring-boot/issues/28046")
  }

  implementation(projects.gradleEnterpriseApiModels)
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("org.springframework.boot:spring-boot-starter-webflux")

  implementation(group = "io.netty", name = "netty-resolver-dns-native-macos", classifier = "osx-aarch_64") {
    because("https://github.com/netty/netty/issues/11020")
  }

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

tasks.processResources.configure {
  from(layout.projectDirectory.file("policy.rego"))
}
