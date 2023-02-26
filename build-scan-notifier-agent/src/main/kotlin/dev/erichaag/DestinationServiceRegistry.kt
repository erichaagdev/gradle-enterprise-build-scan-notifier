package dev.erichaag

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class DestinationServiceRegistry(
  properties: NotifierAgentProperties,
  webClientBuilder: WebClient.Builder,
) {

  final val destinations: Map<String, DestinationService>

  init {
    destinations = properties.slack.destinations.mapValues { (_, properties) ->
      SlackDestinationService(properties, webClientBuilder)
    }
  }

}
