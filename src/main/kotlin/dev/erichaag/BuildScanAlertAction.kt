package dev.erichaag

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.erichaag.model.Build
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class BuildScanAlertAction {

  companion object {
    val logger = LoggerFactory.getLogger(BuildScanAlertAction::class.java)!!
    val objectMapper = jacksonObjectMapper().findAndRegisterModules()!!
  }

  fun alert(build: Build, attributes: Any) = logger.info(objectMapper.writeValueAsString(BuildScanData(build, attributes)))

  private data class BuildScanData(val build: Build, val attributes: Any)
}
