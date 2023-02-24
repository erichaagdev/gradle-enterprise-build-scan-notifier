@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
  id("org.openapi.generator") version "6.4.0"
  id("org.springframework.boot") version "3.0.3"
  kotlin("jvm") version "1.8.10"
  kotlin("plugin.spring") version "1.8.10"
}

group = "dev.erichaag"
version = "0.0.1"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
    vendor.set(JvmVendorSpec.ADOPTIUM)  }
}

dependencies {
  implementation(platform(SpringBootPlugin.BOM_COORDINATES))

  implementation(group = "io.netty", name = "netty-resolver-dns-native-macos", classifier = "osx-aarch_64") {
    because("https://github.com/netty/netty/issues/11020")
  }
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
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

openApiGenerate {
  generatorName.set("java")
  inputSpec.set(layout.projectDirectory.file("gradle-enterprise-2022.4-api.yaml").asFile.absolutePath)
  outputDir.set(layout.buildDirectory.file("generated/openapi").map { it.asFile.absolutePath })
  modelPackage.set("dev.erichaag.model")
  // see https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/java.md for a description of each configuration option
  configOptions.set(mapOf(
    "additionalModelTypeAnnotations" to  "@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)",
    "disallowAdditionalPropertiesIfNotPresent" to "false",
    "hideGenerationTimestamp" to "true",
    "library" to "native",
    "openApiNullable" to "false",
    "sourceFolder" to "",
    "useBeanValidation" to "false",
    "useJakartaEe" to "true",
  ))
}

val processOpenApiGenerate by tasks.registering(Sync::class) {
  from(tasks.openApiGenerate) {
    include("dev/erichaag/model/*.java")
  }
  into(layout.buildDirectory.dir(name))
}

sourceSets {
  main {
    java {
      srcDir(processOpenApiGenerate)
    }
  }
}
