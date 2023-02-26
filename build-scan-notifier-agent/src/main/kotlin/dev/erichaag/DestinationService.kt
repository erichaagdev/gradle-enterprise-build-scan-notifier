package dev.erichaag

import reactor.core.publisher.Mono

interface DestinationService {

  fun sendNotification(message: String): Mono<Void>

}
