package dev.erichaag.source

import dev.erichaag.BuildScan
import dev.erichaag.BuildScanAlertsProperties.ExportApiProperties
import dev.erichaag.model.Build
import dev.erichaag.model.GradleAttributes
import dev.erichaag.model.MavenAttributes
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.net.URI
import java.util.Base64

class ExportApiSourceService(
  properties: ExportApiProperties,
  webClientBuilder: WebClient.Builder
) : SourceService {

  private val apiClient: WebClient

  private val exportApiClient: WebClient

  override val serverUrl: URI

    init {
    val webClient = webClientBuilder.baseUrl(properties.serverUrl.toString()).build()
    val accessKeyBase64 = Base64.getEncoder().encodeToString(properties.accessKey.encodeToByteArray())
    apiClient = webClient.mutate().defaultHeaders { it.setBearerAuth(properties.accessKey) }.build()
    exportApiClient = webClient.mutate().defaultHeaders { it.setBearerAuth(accessKeyBase64) }.build()
    serverUrl = properties.serverUrl
  }

  override fun emitBuildScans() = builds().flatMap { enrichBuild(it) }

  private fun enrichBuild(exportApiBuild: ExportApiBuild): Mono<BuildScan> {
    val link = URI(serverUrl.scheme, serverUrl.host, "/s/${exportApiBuild.buildId}", null)
    val build = getBuild(exportApiBuild.buildId)
    val attributes = when (exportApiBuild.toolType) {
      "gradle" -> getGradleAttributes(exportApiBuild.buildId)
      "maven" -> getMavenAttributes(exportApiBuild.buildId)
      else -> return Mono.empty()
    }
    return Mono.zip(build, attributes).map { (build, attributes) -> BuildScan(link, build, attributes) }
  }

  private fun builds() =
    exportApiClient.get().uri("/build-export/v2/builds/since/now?stream").retrieve().bodyToFlux<ExportApiBuild>()

  private fun getBuild(buildId: String) =
    apiClient.get().uri("/api/builds/{buildId}", buildId).retrieve().bodyToMono<Build>()

  private fun getGradleAttributes(buildId: String) =
    apiClient.get().uri("/api/builds/{buildId}/gradle-attributes", buildId).retrieve().bodyToMono<GradleAttributes>()

  private fun getMavenAttributes(buildId: String) =
    apiClient.get().uri("/api/builds/{buildId}/maven-attributes", buildId).retrieve().bodyToMono<MavenAttributes>()

  private data class ExportApiBuild(val buildId: String, val toolType: String)
}
