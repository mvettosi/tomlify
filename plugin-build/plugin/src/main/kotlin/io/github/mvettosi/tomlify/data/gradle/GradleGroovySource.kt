@file:Suppress("UNUSED_PARAMETER")

package io.github.mvettosi.tomlify.data.gradle

import io.github.mvettosi.tomlify.domain.source.GradleSource
import org.gradle.api.Project

class GradleGroovySource(project: Project) : GradleSource {
  override fun readFiles(read: (List<String>) -> Unit) {
    // TODO("Not yet implemented")
  }

  override fun writeFiles(transform: MutableList<String>.() -> Unit) {
    // TODO("Not yet implemented")
  }
}
