package dev.erichaag

import dev.erichaag.destination.DestinationServiceRegistry
import dev.erichaag.policy.Notification
import dev.erichaag.policy.PolicyService
import dev.erichaag.source.SourceServiceRegistry
import org.springframework.beans.factory.DisposableBean
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import reactor.core.Disposable
import reactor.core.publisher.Mono

@Component
class NotifierAgentRunner(
  private val sourceServices: SourceServiceRegistry,
  private val policyService: PolicyService,
  private val destinationServices: DestinationServiceRegistry,
) : ApplicationRunner, DisposableBean {

  private var disposables: Set<Disposable> = setOf()

  override fun run(args: ApplicationArguments) {
    disposables = sourceServices.sources.map { source ->
      source
        .emitBuildScans()
        .flatMap { policyService.getNotifications(it) }
        .flatMapIterable { it }
        .flatMap(this::sendNotification)
        .subscribe()
    }.toSet()
  }

  private fun sendNotification(notification: Notification) =
    destinationServices.destinations[notification.destination]?.sendNotification(notification.message) ?: Mono.empty()

  override fun destroy() = disposables.forEach { it.dispose() }

}
