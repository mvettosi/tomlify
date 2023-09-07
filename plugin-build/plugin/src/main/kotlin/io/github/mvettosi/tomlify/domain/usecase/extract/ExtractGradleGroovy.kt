@file:Suppress("unused")

package io.github.mvettosi.tomlify.domain.usecase.extract

import io.github.mvettosi.tomlify.domain.model.Dependency
import io.github.mvettosi.tomlify.domain.source.GradleSource

class ExtractGradleGroovy(private val gradleSource: GradleSource) {
  operator fun invoke(): List<Dependency> {
    // TODO if there's request
    return emptyList()
  }
}
