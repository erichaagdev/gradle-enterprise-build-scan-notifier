package dev.erichaag.source

import dev.erichaag.BuildScanAlertsProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class SourceServiceRegistry(
  properties: BuildScanAlertsProperties,
  webClientBuilder: WebClient.Builder,
) {

  final val sources: Set<SourceService>

  init {
    sources = properties.sources.exportApi.values.map { ExportApiSourceService(it, webClientBuilder) }.toSet()
  }

}
