package dev.erichaag.policy

import dev.erichaag.BuildScan
import reactor.core.publisher.Mono

interface PolicyService {

  fun getAlerts(buildScan: BuildScan): Mono<Set<PolicyDecision>>

}
