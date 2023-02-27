package dev.erichaag

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("notification")
data class NotificationProperties(
  var rules: Map<String, Rule>,
) {

  data class Rule(
    var message: String,
    var destinations: Set<String>,
    var condition: Condition,
  )

  data class Condition(
    var outcome: Outcome = Outcome.FAILED,
    var projects: Set<String> = emptySet(),
    var tags: Set<String> = emptySet(),
  )

  @Suppress("unused")
  enum class Outcome {
    SUCCEEDED,
    FAILED,
    ANY,
  }

}
