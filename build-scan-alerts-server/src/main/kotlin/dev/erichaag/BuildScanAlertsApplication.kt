package dev.erichaag

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class BuildScanAlertsApplication

fun main(args: Array<String>) {
  runApplication<BuildScanAlertsApplication>(*args)
}
