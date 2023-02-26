package dev.erichaag.destination

import dev.erichaag.NotifierAgentProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class DestinationServiceRegistry(
  properties: NotifierAgentProperties,
  webClientBuilder: WebClient.Builder,
) {

  final val destinations: Map<String, DestinationService>

  init {
    destinations = properties.destinations.slackWebhook.mapValues { (_, properties) ->
      SlackWebhookService(properties, webClientBuilder)
    }
  }

}
