package dev.erichaag

import dev.erichaag.GradleEnterpriseProperties.GradleEnterpriseServer
import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI

/**
 * Configuration properties for Gradle Enterprise servers.
 *
 * @property servers A map of [GradleEnterpriseServer] objects, where the key is the name of the Gradle Enterprise
 * server.
 */
@ConfigurationProperties("gradle-enterprise")
data class GradleEnterpriseProperties(val servers: Map<String, GradleEnterpriseServer>) {

  /**
   * A Gradle Enterprise server that the agent should listen to.
   *
   * @property serverUrl The URL of the Gradle Enterprise server.
   * @property accessKey The access key to use when authenticating with the Gradle Enterprise server.
   */
  data class GradleEnterpriseServer(
    var serverUrl: URI,
    var accessKey: String,
  )

}
