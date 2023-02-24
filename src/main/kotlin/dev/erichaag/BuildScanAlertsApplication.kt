package dev.erichaag

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BuildScanAlertsApplication

fun main(args: Array<String>) {
  runApplication<BuildScanAlertsApplication>(*args)
}
