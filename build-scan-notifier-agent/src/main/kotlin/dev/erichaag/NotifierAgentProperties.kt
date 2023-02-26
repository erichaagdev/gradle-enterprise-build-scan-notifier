package dev.erichaag

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI

@ConfigurationProperties("notifier")
data class NotifierAgentProperties(
  val sources: SourceProperties,
  val policy: PolicyProperties,
  val destinations: DestinationProperties,
) {

  data class DestinationProperties(var slackWebhook: Map<String, SlackWebhookProperties>)
  data class SlackWebhookProperties(var webhookUrl: URI)

  data class SourceProperties(var exportApi: Map<String, ExportApiProperties>)
  data class ExportApiProperties(var serverUrl: URI, var accessKey: String)

  data class PolicyProperties(var openPolicyAgent: OpenPolicyAgentProperties)
  data class OpenPolicyAgentProperties(var agentUrl: URI)
}
