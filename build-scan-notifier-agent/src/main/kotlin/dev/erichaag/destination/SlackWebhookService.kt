package dev.erichaag.destination

import dev.erichaag.NotifierAgentProperties.SlackWebhookProperties
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class SlackWebhookService(
  properties: SlackWebhookProperties,
  webClientBuilder: WebClient.Builder,
) : DestinationService {

  private val client: WebClient

  init {
    client = webClientBuilder.baseUrl(properties.webhookUrl.toString()).build()
  }

  override fun sendNotification(message: String) =
    client
      .post()
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(WebhookRequest(message))
      .retrieve()
      .bodyToMono<Void>()

  private data class WebhookRequest(val text: String)
}
