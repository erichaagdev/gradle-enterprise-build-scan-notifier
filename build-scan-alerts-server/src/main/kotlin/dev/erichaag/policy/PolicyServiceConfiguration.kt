package dev.erichaag.policy

import dev.erichaag.BuildScanAlertsProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient.Builder

@Configuration
class PolicyServiceConfiguration {

  @Bean
  fun policyService(webClientBuilder: Builder, properties: BuildScanAlertsProperties): PolicyService =
    OpenPolicyAgentPolicyService(properties.policy.openPolicyAgent, webClientBuilder)

}
