package dev.erichaag

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "")
data class HelloProperties(
  var hello: String
)
