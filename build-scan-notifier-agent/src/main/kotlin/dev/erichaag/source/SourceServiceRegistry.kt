package dev.erichaag.source

import dev.erichaag.NotifierAgentProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class SourceServiceRegistry(
  properties: NotifierAgentProperties,
  webClientBuilder: WebClient.Builder,
) {

  final val sources: Set<SourceService>

  init {
    sources = properties.sources.exportApi.values.map { ExportApiSourceService(it, webClientBuilder) }.toSet()
  }

}
