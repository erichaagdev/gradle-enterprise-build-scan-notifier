package dev.erichaag

import java.net.URI

data class SlackProperties(var destinations: Map<String, SlackDestination>) {
  data class SlackDestination(var webhookUrl: URI)
}
