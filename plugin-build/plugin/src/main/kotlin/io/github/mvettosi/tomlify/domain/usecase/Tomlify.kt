package io.github.mvettosi.tomlify.domain.usecase

import io.github.mvettosi.tomlify.domain.usecase.extract.ExtractDependencies
import io.github.mvettosi.tomlify.domain.usecase.replace.ReplaceDependencies
import io.github.mvettosi.tomlify.domain.usecase.update.UpdateCatalog

class Tomlify(
    private val extractDependencies: ExtractDependencies,
    private val updateCatalog: UpdateCatalog,
    private val replaceDependencies: ReplaceDependencies
) {
  operator fun invoke() {
    val dependencies = extractDependencies()
    updateCatalog(dependencies)
    replaceDependencies(dependencies)
  }
}
