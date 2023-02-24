package dev.erichaag

import dev.erichaag.model.Build
import dev.erichaag.model.ExportApiBuild
import dev.erichaag.model.GradleAttributes
import dev.erichaag.model.MavenAttributes
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.client.bodyToMono

class GradleEnterpriseApiClient(
  private val api: WebClient,
  private val exportApi: WebClient,
) {

  fun stream() =
    exportApi.get().uri("/build-export/v2/builds/since/now?stream").retrieve().bodyToFlux<ExportApiBuild>()

  fun getBuild(buildId: String) =
    api.get().uri("/api/builds/{buildId}", buildId).retrieve().bodyToMono<Build>()

  fun getGradleAttributes(buildId: String) =
    api.get().uri("/api/builds/{buildId}/gradle-attributes", buildId).retrieve().bodyToMono<GradleAttributes>()

  fun getMavenAttributes(buildId: String) =
    api.get().uri("/api/builds/{buildId}/maven-attributes", buildId).retrieve().bodyToMono<MavenAttributes>()
}
