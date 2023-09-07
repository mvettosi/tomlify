package io.github.mvettosi.tomlify.domain.usecase.replace

import io.github.mvettosi.tomlify.domain.model.Dependency

class ReplaceDependencies(
    private val replaceGradkeKts: ReplaceGradkeKts,
    private val replaceGradleGroovy: ReplaceGradleGroovy
) {
  operator fun invoke(dependencies: List<Dependency>) {
    replaceGradkeKts(dependencies)
    replaceGradleGroovy(dependencies)
  }
}
