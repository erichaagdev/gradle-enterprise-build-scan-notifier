package dev.erichaag

import dev.erichaag.model.Build
import dev.erichaag.model.ExportApiBuild
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import reactor.util.function.Tuple2

@Component
class BuildScanAlertsRunner(
  private val client: GradleEnterpriseApiClient,
  private val policyClient: OpenPolicyAgentClient,
  private val action: BuildScanAlertAction,
) : ApplicationRunner {

  override fun run(args: ApplicationArguments) {
    client.stream().flatMap(this::processBuild)
      .filterWhen { (build, attributes) -> policyClient.shouldAlert(build, attributes) }
      .doOnNext { (build, attributes) -> action.alert(build, attributes) }
      .subscribe()
  }

  private fun processBuild(build: ExportApiBuild): Mono<Tuple2<Build, Any>> {
    val buildResponse = client.getBuild(build.buildId)
    return if (build.isGradle()) {
      buildResponse.zipWith(client.getGradleAttributes(build.buildId))
    } else if (build.isMaven()) {
      buildResponse.zipWith(client.getMavenAttributes(build.buildId))
    } else {
      Mono.empty()
    }
  }
}
