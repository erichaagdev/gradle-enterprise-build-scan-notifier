package dev.erichaag

import com.gradle.enterprise.model.GradleAttributes
import com.gradle.enterprise.model.MavenAttributes
import dev.erichaag.NotificationProperties.Outcome.FAILED
import dev.erichaag.NotificationProperties.Outcome.SUCCEEDED
import dev.erichaag.NotificationProperties.Rule
import org.apache.commons.text.StringSubstitutor
import org.springframework.stereotype.Service
import java.net.URI

@Service
class NotificationService(
  private val properties: NotificationProperties,
) {

  fun getNotifications(buildScan: BuildScan): Set<Notification> {
    val attributes = extractCommonAttributes(buildScan.attributes) ?: return emptySet()
    return properties.rules.values
      .filter { rule -> shouldNotify(rule, attributes) }
      .flatMap { rule -> buildNotifications(rule, buildScan.link, attributes.projectName) }
      .toSet()
  }

  private fun shouldNotify(rule: Rule, attributes: CommonAttributes) = with(attributes) {
    val desired = rule.condition
    if (desired.outcome == FAILED && !failed) return@with false
    if (desired.outcome == SUCCEEDED && failed) return@with false
    if (desired.projects.isNotEmpty() && !desired.projects.allLowercase().contains(projectName)) return@with false
    desired.tags.allLowercase().forEach { tag ->
      if (tag.startsWith("not:") && tags.contains(tag.removePrefix("not:"))) return@with false
      if (!tag.startsWith("not:") && !tags.contains(tag)) return@with false
    }
    return@with true
  }

  private fun buildNotifications(rule: Rule, link: URI, projectName: String) =
    rule.destinations.map {
      val substitution = mapOf("link" to link.toString(), "projectName" to projectName)
      val substitutor = StringSubstitutor(substitution, "%{", "}", '%')
      val message = substitutor.replace(rule.message)
      Notification(it, message)
    }

  private fun extractCommonAttributes(attributes: Any) =
    when (attributes) {
      is GradleAttributes -> CommonAttributes(attributes)
      is MavenAttributes -> CommonAttributes(attributes)
      else -> null
    }

  private class CommonAttributes {

    val failed: Boolean
    val projectName: String
    val tags: Set<String>

    constructor(attributes: GradleAttributes) {
      failed = attributes.hasFailed
      projectName = attributes.rootProjectName
      tags = attributes.tags.toSet().allLowercase()
    }

    constructor(attributes: MavenAttributes) {
      failed = attributes.hasFailed
      projectName = attributes.topLevelProjectName
      tags = attributes.tags.toSet().allLowercase()
    }
  }

}

private fun Set<String>.allLowercase() = map { it.lowercase() }.toSet()
