package dev.erichaag

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class NotifierAgentApplication

fun main(args: Array<String>) {
  runApplication<NotifierAgentApplication>(*args)
}
