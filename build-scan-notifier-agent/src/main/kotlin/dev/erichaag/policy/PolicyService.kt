package dev.erichaag.policy

import dev.erichaag.BuildScan
import reactor.core.publisher.Mono

interface PolicyService {

  fun getNotifications(buildScan: BuildScan): Mono<Set<Notification>>

}
