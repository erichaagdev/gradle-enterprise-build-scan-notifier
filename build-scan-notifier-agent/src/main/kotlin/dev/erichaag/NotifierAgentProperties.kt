package dev.erichaag

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties("notifier")
data class NotifierAgentProperties(
  @NestedConfigurationProperty val exportApi: ExportApiProperties,
  @NestedConfigurationProperty val slack: SlackProperties,
)
