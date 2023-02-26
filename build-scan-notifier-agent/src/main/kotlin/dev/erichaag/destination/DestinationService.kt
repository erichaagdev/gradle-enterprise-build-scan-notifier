package dev.erichaag.destination

import reactor.core.publisher.Mono

interface DestinationService {

  fun sendAlert(message: String): Mono<Void>

}
