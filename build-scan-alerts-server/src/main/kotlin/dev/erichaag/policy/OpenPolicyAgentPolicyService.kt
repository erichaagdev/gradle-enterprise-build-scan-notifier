package dev.erichaag.policy

import dev.erichaag.BuildScan
import dev.erichaag.BuildScanAlertsProperties.OpenPolicyAgentProperties
import org.springframework.http.MediaType
import org.springframework.util.ResourceUtils
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class OpenPolicyAgentPolicyService(
  properties: OpenPolicyAgentProperties,
  webClientBuilder: WebClient.Builder,
) : PolicyService {

  private val client: WebClient

  init {
    client = webClientBuilder.baseUrl(properties.agentUrl.toString()).build()
  }

  override fun getAlerts(buildScan: BuildScan): Mono<Set<PolicyDecision>> = client.post()
    .uri("/v1/data/alerts")
    .contentType(MediaType.APPLICATION_JSON)
    .bodyValue(DecisionRequest(buildScan))
    .retrieve()
    .bodyToMono<DecisionResponse>()
    .map { it.result.alerts }

  fun loadPolicyFromClasspath() = client.put()
    .uri("/v1/policies/alerts")
    .contentType(MediaType.TEXT_PLAIN)
    .bodyValue(ResourceUtils.getFile("policy.rego").readText())
    .retrieve()
    .bodyToMono<Void>()

  private data class DecisionRequest(val input: BuildScan)

  private data class DecisionResponse(val result: Result)

  private data class Result(val alerts: Set<PolicyDecision>)
}
