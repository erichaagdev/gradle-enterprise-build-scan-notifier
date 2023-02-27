package dev.erichaag

import dev.erichaag.SlackProperties.SlackWebhook
import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI

/**
 * Configuration properties for Slack webhooks.
 *
 * @property webhooks A map of [SlackWebhook] objects, where the key is the name of the webhook.
 */
@ConfigurationProperties("slack")
data class SlackProperties(var webhooks: Map<String, SlackWebhook>) {

  /**
   * A Slack webhook that the agent can use to send notifications to a Slack channel.
   *
   * @property webhookUrl The URL of the Slack webhook.
   */
  data class SlackWebhook(var webhookUrl: URI)
}
