package dev.erichaag

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class SourceServiceRegistry(
  gradleEnterprise: GradleEnterpriseProperties,
  webClientBuilder: WebClient.Builder,
) {

  final val sources: Set<SourceService>

  init {
    sources = gradleEnterprise.servers.values.map { GradleEnterpriseSourceService(it, webClientBuilder) }.toSet()
  }

}
