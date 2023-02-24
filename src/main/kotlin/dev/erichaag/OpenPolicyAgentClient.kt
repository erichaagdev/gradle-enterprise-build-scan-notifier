package dev.erichaag

import dev.erichaag.model.Build
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class OpenPolicyAgentClient(
  private val webClient: WebClient,
) {

  fun shouldAlert(build: Build, attributes: Any) = webClient.post()
    .uri("/v1/data/alerts")
    .contentType(MediaType.APPLICATION_JSON)
    .bodyValue(PolicyDecisionRequest(BuildScanData(build, attributes)))
    .retrieve()
    .bodyToMono<PolicyDecisionResponse>()
    .map { it.result.alert }

  private data class BuildScanData(val build: Build, val attributes: Any)

  private data class PolicyDecisionRequest(val input: BuildScanData)

  private data class PolicyDecisionResponse(val result: PolicyDecisionResponseResult)

  private data class PolicyDecisionResponseResult(val alert: Boolean)
}
