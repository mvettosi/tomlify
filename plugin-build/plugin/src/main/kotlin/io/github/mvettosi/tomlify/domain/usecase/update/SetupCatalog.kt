package io.github.mvettosi.tomlify.domain.usecase.update

import io.github.mvettosi.tomlify.domain.model.TomlHeader
import io.github.mvettosi.tomlify.domain.source.CatalogSource

class SetupCatalog(private val catalogSource: CatalogSource) {
  operator fun invoke() {
    catalogSource.initialiseCatalog(
        """
        ${TomlHeader.VERSIONS}

        ${TomlHeader.BUNDLES}

        ${TomlHeader.LIBRARIES}

        ${TomlHeader.PLUGINS}
      """
            .trimIndent())
  }
}
