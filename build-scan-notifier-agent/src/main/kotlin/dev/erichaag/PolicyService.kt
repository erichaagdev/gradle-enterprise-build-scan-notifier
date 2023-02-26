package dev.erichaag

import reactor.core.publisher.Mono

interface PolicyService {

  fun getNotifications(buildScan: BuildScan): Mono<Set<Notification>>

}
