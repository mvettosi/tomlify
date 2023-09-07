package io.github.mvettosi.tomlify.domain.source

interface CatalogSource {
  fun isCatalogConfigured(): Boolean
  fun initialiseCatalog(content: String)
  fun updateCatalog(transform: MutableList<String>.() -> Unit)
  enum class TomlHeader(private val string: String) {
    VERSIONS("[versions]"),
    LIBRARIES("[libraries]"),
    BUNDLES("[bundles]"),
    PLUGINS("[plugins]");

    override fun toString() = string
  }
}
