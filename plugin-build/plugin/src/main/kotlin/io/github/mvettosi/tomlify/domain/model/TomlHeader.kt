package io.github.mvettosi.tomlify.domain.model

enum class TomlHeader(private val string: String) {
  VERSIONS("[versions]"),
  LIBRARIES("[libraries]"),
  BUNDLES("[bundles]"),
  PLUGINS("[plugins]");

  override fun toString() = string
}
