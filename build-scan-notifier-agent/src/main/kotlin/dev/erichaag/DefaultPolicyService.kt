package dev.erichaag

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DefaultPolicyService : PolicyService {

  override fun getNotifications(buildScan: BuildScan): Mono<Set<Notification>> {
    TODO("Not yet implemented")
  }

}
