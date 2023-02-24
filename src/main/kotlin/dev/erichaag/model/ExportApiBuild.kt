package dev.erichaag.model

import java.time.Instant

data class ExportApiBuild(
  val agentVersion: String,
  val buildId: String,
  val timestamp: Instant,
  val toolType: String,
  val toolVersion: String,
) {

  fun isGradle() = toolType == "gradle"

  fun isMaven() = toolType == "maven"
}
