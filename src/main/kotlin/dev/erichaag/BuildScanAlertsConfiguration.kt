package dev.erichaag

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import java.util.Base64

@Configuration
class BuildScanAlertsConfiguration {

  @Bean
  fun gradleEnterpriseApiClient(webClientBuilder: WebClient.Builder): GradleEnterpriseApiClient {
    val accessKey = System.getenv("GRADLE_ENTERPRISE_API_ACCESS_KEY")
    val webClient = webClientBuilder.baseUrl("https://ge.solutions-team.gradle.com").build()
    val api = webClient.mutate().defaultHeaders { it.setBearerAuth(accessKey) }.build()
    val exportApi = webClient.mutate().defaultHeaders { it.setBearerAuth(accessKey.base64()) }.build()
    return GradleEnterpriseApiClient(api, exportApi)
  }

  @Bean
  fun openPolicyAgentClient(webClientBuilder: WebClient.Builder): OpenPolicyAgentClient {
    val webClient = webClientBuilder.baseUrl("http://localhost:8181").build()
    return OpenPolicyAgentClient(webClient)
  }

  private fun String.base64() = Base64.getEncoder().encodeToString(encodeToByteArray())

  @Bean
  fun helloPrinter(helloProperties: HelloProperties) = ApplicationRunner {
    println(helloProperties.hello)
  }
}
