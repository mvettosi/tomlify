package io.github.mvettosi.tomlify.domain.source

interface CatalogSource {
  fun isCatalogConfigured(): Boolean
  fun initialiseCatalog(content: String)
  fun updateCatalog(transform: MutableList<String>.() -> Unit)
}
