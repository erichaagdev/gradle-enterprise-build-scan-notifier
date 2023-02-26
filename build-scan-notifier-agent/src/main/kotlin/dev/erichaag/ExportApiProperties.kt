package dev.erichaag

import java.net.URI

data class ExportApiProperties(
  val sources: Map<String, ExportApiSource>,
) {

  data class ExportApiSource(
    var serverUrl: URI,
    var accessKey: String,
  )

}
