package dev.erichaag

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class DestinationServiceRegistry(
  slack: SlackProperties,
  webClientBuilder: WebClient.Builder,
) {

  final val destinations: Map<String, DestinationService>

  init {
    destinations = slack.webhooks.mapValues { (_, properties) ->
      SlackDestinationService(properties, webClientBuilder)
    }
  }

}
