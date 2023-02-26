package dev.erichaag

import com.gradle.enterprise.model.Build
import java.net.URI

data class BuildScan(val link: URI, val build: Build, val attributes: Any)
