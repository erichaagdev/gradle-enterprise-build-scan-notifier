package dev.erichaag

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import reactor.core.Disposable
import reactor.core.publisher.Mono

@Component
class NotifierAgentRunner(
  private val destinationServices: DestinationServiceRegistry,
  private val notificationService: NotificationService,
  private val sourceServices: SourceServiceRegistry,
) : ApplicationRunner, DisposableBean {

  companion object {
    private val log = LoggerFactory.getLogger(NotifierAgentRunner::class.java)
    private val objectMapper = jacksonObjectMapper().findAndRegisterModules()
  }

  private var disposables: Set<Disposable> = setOf()

  override fun run(args: ApplicationArguments) {
    disposables = sourceServices.sources.map { source ->
      source
        .emitBuildScans()
        .doOnNext { log.info(objectMapper.writeValueAsString(it)) }
        .flatMapIterable { notificationService.getNotifications(it) }
        .flatMap(this::sendNotification)
        .subscribe()
    }.toSet()
  }

  private fun sendNotification(notification: Notification) =
    destinationServices.destinations[notification.destination]?.sendNotification(notification.message) ?: Mono.empty()

  override fun destroy() = disposables.forEach { it.dispose() }

}
