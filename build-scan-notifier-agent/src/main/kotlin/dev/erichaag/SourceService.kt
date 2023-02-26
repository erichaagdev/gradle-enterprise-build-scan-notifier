package dev.erichaag

import reactor.core.publisher.Flux
import java.net.URI

interface SourceService {

  val serverUrl: URI

  fun emitBuildScans(): Flux<BuildScan>

}
