package io.github.mvettosi.tomlify.data.toml

import io.github.mvettosi.tomlify.domain.source.CatalogSource
import java.io.File
import java.nio.file.Files
import kotlin.io.path.writeLines

class TomlCatalogSource(private val tomlLocation: File) : CatalogSource {
  override fun isCatalogConfigured(): Boolean = tomlLocation.exists()

  override fun initialiseCatalog(content: String) {
    if (isCatalogConfigured()) {
      throw IllegalStateException("Cannot create a TOML file because one exists already")
    }
    Files.createDirectories(tomlLocation.toPath().parent)
    tomlLocation.createNewFile()
    tomlLocation.writeText(content)
  }

  override fun updateCatalog(transform: MutableList<String>.() -> Unit) {
    if (!isCatalogConfigured()) {
      throw IllegalStateException("An existing TOML file is required to add lines to it!")
    }
    val lines = tomlLocation.readLines().toMutableList()
    lines.transform()
    tomlLocation.toPath().writeLines(lines)
  }
}
