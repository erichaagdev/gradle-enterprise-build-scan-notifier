enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
  id("com.gradle.common-custom-user-data-gradle-plugin") version "1.13"
  id("com.gradle.enterprise") version "3.16.2"
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

val isCI = System.getenv().containsKey("CI")

gradleEnterprise {
  if (!providers.gradleProperty("agreeToGradleTermsOfService").orNull.toBoolean()) return@gradleEnterprise
  buildScan {
    isUploadInBackground = !isCI
    termsOfServiceAgree = "yes"
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    publishAlways()
  }
}

include("build-scan-notifier-agent")
include("gradle-enterprise-api-models")

rootProject.name = "gradle-enterprise-build-scan-notifier"
