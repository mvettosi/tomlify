package io.github.mvettosi.tomlify.domain.usecase.update

import io.github.mvettosi.tomlify.domain.source.CatalogSource

class SetupCatalog(private val catalogSource: CatalogSource) {
  operator fun invoke() {
    catalogSource.initialiseCatalog(
        """
        ${CatalogSource.TomlHeader.VERSIONS}

        ${CatalogSource.TomlHeader.BUNDLES}

        ${CatalogSource.TomlHeader.LIBRARIES}

        ${CatalogSource.TomlHeader.PLUGINS}
      """
            .trimIndent())
  }
}
