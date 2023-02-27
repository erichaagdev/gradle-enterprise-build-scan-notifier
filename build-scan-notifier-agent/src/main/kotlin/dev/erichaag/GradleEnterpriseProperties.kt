package dev.erichaag

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI

@ConfigurationProperties("gradle-enterprise")
data class GradleEnterpriseProperties(val servers: Map<String, GradleEnterpriseServer>) {

  data class GradleEnterpriseServer(
    var serverUrl: URI,
    var accessKey: String,
  )

}
