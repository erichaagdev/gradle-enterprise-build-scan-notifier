package dev.erichaag

import dev.erichaag.model.Build
import java.net.URI

data class BuildScan(val link: URI, val build: Build, val attributes: Any)
