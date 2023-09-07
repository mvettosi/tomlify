package io.github.mvettosi.tomlify.domain.usecase.update

import io.github.mvettosi.tomlify.domain.model.Dependency
import io.github.mvettosi.tomlify.domain.model.VersionString
import io.github.mvettosi.tomlify.domain.source.CatalogSource

class UpdateCatalog(
    private val catalogSource: CatalogSource,
    private val setupCatalog: SetupCatalog
) {
  operator fun invoke(dependencies: List<Dependency>) {
    if (!catalogSource.isCatalogConfigured()) setupCatalog()

    catalogSource.updateCatalog {
      dependencies.forEach { dependency ->
        addVersionFrom(dependency)
        addDeclarationFrom(dependency)
      }
    }
  }
}

private fun MutableList<String>.addVersionFrom(dependency: Dependency) {
  if (dependency.version.isBlank()) return

  val newVersionLine = dependency.toTomlVersionLine()
  val existingVersionLineIndex = indexOfFirst { it.startsWith(dependency.getVersionId()) }

  if (existingVersionLineIndex >= 0) {
    val existingVersion =
        VersionString(
            get(existingVersionLineIndex).substringAfterLast("=").trim().replace("\"", ""))
    if (dependency.version > existingVersion) {
      set(existingVersionLineIndex, newVersionLine)
    }
  } else {
    add(insertionFor(CatalogSource.TomlHeader.VERSIONS), newVersionLine)
  }
}

private fun MutableList<String>.addDeclarationFrom(dependency: Dependency) {
  val insertion =
      when (dependency) {
        is Dependency.Library -> insertionFor(CatalogSource.TomlHeader.LIBRARIES)
        is Dependency.Plugin -> insertionFor(CatalogSource.TomlHeader.PLUGINS)
      }
  val newTomlLine = dependency.toTomlLine()
  if (!contains(newTomlLine)) {
    add(insertion, newTomlLine)
  }
}

private fun List<String>.insertionFor(startSection: CatalogSource.TomlHeader): Int {
  for (i in indexOf(startSection.toString()) until size) {
    if (get(i).isBlank()) return i
  }
  return size
}

private fun Dependency.toTomlVersionLine() = "${getVersionId()} = \"$version\""

private fun Dependency.toTomlLine() = buildString {
  when (this@toTomlLine) {
    is Dependency.Library -> append("${toTomlId()} = { group = \"${group}\", name = \"${name}\"")
    is Dependency.Plugin -> append("${toTomlId()} = { id = \"${id}\"")
  }
  if (this@toTomlLine.version.isNotBlank()) {
    append(", version.ref = \"${getVersionId()}\"")
  }
  append(" }")
}

private fun Dependency.getVersionId() = "${toTomlId()}-version"

private fun Dependency.toTomlId() = getIdentifier().replace(".", "-")
