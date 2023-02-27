package dev.erichaag

import dev.erichaag.NotificationProperties.Rule
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration properties for defining notification rules.
 *
 * @property rules A map of [Rule] objects, where the key is the name of the rule.
 */
@ConfigurationProperties("notification")
data class NotificationProperties(
  var rules: Map<String, Rule>,
) {

  /**
   * A rule that defines when to send a notification, what the notification will say, and where the notification should
   * be sent.
   *
   * @property condition The condition under which the notification will be sent.
   * @property destinations The destinations to send the notification to.
   * @property message The message to send.
   */
  data class Rule(
    var condition: Condition,
    var destinations: Set<String>,
    var message: String,
  )

  /**
   * The condition under which a notification will be sent.
   *
   * @property outcome The outcome of the build that triggers the notification. Defaults to [Outcome.FAILED].
   * @property projects A set of project names that the condition applies to. An empty set applies to all projects.
   * @property tags A set of tags that the condition applies to.
   */
  data class Condition(
    var outcome: Outcome = Outcome.FAILED,
    var projects: Set<String> = emptySet(),
    var tags: Set<String> = emptySet(),
  )

  /**
   * An enum representing the possible build outcomes.
   */
  @Suppress("unused")
  enum class Outcome {
    SUCCEEDED,
    FAILED,
    ANY,
  }

}
