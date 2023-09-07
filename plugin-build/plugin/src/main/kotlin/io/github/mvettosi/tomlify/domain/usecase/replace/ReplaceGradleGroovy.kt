@file:Suppress("UNUSED_PARAMETER")

package io.github.mvettosi.tomlify.domain.usecase.replace

import io.github.mvettosi.tomlify.domain.model.Dependency
import io.github.mvettosi.tomlify.domain.source.GradleSource

class ReplaceGradleGroovy(private val gradleSource: GradleSource) {
  operator fun invoke(dependencies: List<Dependency>) {
    // TODO if there's request
  }
}
