package dev.erichaag

import dev.erichaag.destination.DestinationServiceRegistry
import dev.erichaag.policy.OpenPolicyAgentPolicyService
import dev.erichaag.policy.PolicyDecision
import dev.erichaag.policy.PolicyService
import dev.erichaag.source.SourceServiceRegistry
import org.springframework.beans.factory.DisposableBean
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import reactor.core.Disposable
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class BuildScanAlertsRunner(
  private val sourceServices: SourceServiceRegistry,
  private val policyService: PolicyService,
  private val destinationServices: DestinationServiceRegistry,
) : ApplicationRunner, DisposableBean {

  private var disposables: Set<Disposable> = setOf()

  override fun run(args: ApplicationArguments) {
    if (policyService is OpenPolicyAgentPolicyService) {
      policyService.loadPolicyFromClasspath().block(Duration.ofSeconds(10))
    }
    disposables = sourceServices.sources.map { source ->
      source
        .emitBuildScans()
        .flatMap { policyService.getAlerts(it) }
        .flatMapIterable { it }
        .flatMap(this::sendAlert)
        .subscribe()
    }.toSet()
  }

  private fun sendAlert(alert: PolicyDecision) =
    destinationServices.destinations[alert.destination]?.sendAlert(alert.message) ?: Mono.empty()

  override fun destroy() = disposables.forEach { it.dispose() }

}
