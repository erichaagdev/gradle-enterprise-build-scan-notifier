plugins {
  id("java-library")
  id("org.openapi.generator") version "7.0.1"
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
    vendor.set(JvmVendorSpec.ADOPTIUM)
  }
}

repositories {
  exclusiveContent {
    forRepository {
      ivy {
        url = uri("https://docs.gradle.com/enterprise/api-manual/ref")
        patternLayout { artifact("gradle-enterprise-[revision]-api.[ext]") }
        metadataSources { artifact() }
      }
    }
    filter { includeModule("com.gradle.enterprise", "gradle-enterprise-api-specification") }
  }
  mavenCentral()
}

val apiSpecification: Configuration by configurations.creating

dependencies {
  apiSpecification("com.gradle.enterprise:gradle-enterprise-api-specification:2022.4@yaml")

  api("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
  api("jakarta.annotation:jakarta.annotation-api:2.1.1")
}

openApiGenerate {
  generatorName.set("java")
  inputSpec.set(providers.provider { apiSpecification.singleFile.absolutePath })
  outputDir.set(layout.buildDirectory.file("generated/openapi").map { it.asFile.absolutePath })
  modelPackage.set("com.gradle.enterprise.model")
  // see https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/java.md for a description of each configuration option
  configOptions.set(
    mapOf(
      "additionalModelTypeAnnotations" to "@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)",
      "disallowAdditionalPropertiesIfNotPresent" to "false",
      "hideGenerationTimestamp" to "true",
      "library" to "native",
      "openApiNullable" to "false",
      "sourceFolder" to "",
      "useBeanValidation" to "false",
      "useJakartaEe" to "true",
    )
  )
}

val processOpenApiGenerate by tasks.registering(Sync::class) {
  from(tasks.openApiGenerate) {
    include("com/gradle/enterprise/model/*.java")
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
