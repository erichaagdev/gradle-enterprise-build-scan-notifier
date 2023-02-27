package dev.erichaag

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI

@ConfigurationProperties("slack")
data class SlackProperties(var webhooks: Map<String, SlackWebhook>) {
  data class SlackWebhook(var webhookUrl: URI)
}
