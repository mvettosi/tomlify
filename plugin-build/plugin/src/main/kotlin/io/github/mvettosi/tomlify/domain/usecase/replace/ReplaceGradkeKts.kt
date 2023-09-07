package io.github.mvettosi.tomlify.domain.usecase.replace

import io.github.mvettosi.tomlify.domain.model.Dependency
import io.github.mvettosi.tomlify.domain.model.GradleKtsConstants
import io.github.mvettosi.tomlify.domain.model.containsAny
import io.github.mvettosi.tomlify.domain.source.GradleSource

class ReplaceGradkeKts(private val gradleSource: GradleSource) {
  operator fun invoke(dependencies: List<Dependency>) {
    gradleSource.writeFiles {
      forEachIndexed { index, line ->
        when {
          !line.mightHaveLegacyReference() -> {} // If no legacy reference, nothing to replace
          line.isLibrary() -> replaceLibrary(index, line, dependencies)
          line.isPluginDeclaration() -> replacePluginDeclaration(index, line, dependencies)
          line.isPluginApplication() -> replacePluginApplication(index, line, dependencies)
        }
      }
    }
  }
}

private fun String.mightHaveLegacyReference(): Boolean = """.*\(".*"\).*""".toRegex().matches(this)

private fun MutableList<String>.replaceLibrary(
    index: Int,
    oldLine: String,
    dependencies: List<Dependency>
) {
  val library = dependencies.firstLibraryMatching(oldLine) ?: return
  val oldDeclarationRegex = """"${library.getReference()}[^"]*"""".toRegex()
  val newDeclaration = "libs.${library.toGradleReference()}"
  val selector = library.selector?.let { " { artifact { type = \"${it}\" } }" } ?: ""
  val newLine = oldLine.replace(oldDeclarationRegex, newDeclaration) + selector
  set(index, newLine)
}

private fun MutableList<String>.replacePluginDeclaration(
    index: Int,
    oldLine: String,
    dependencies: List<Dependency>
) =
    replacePlugin(
        index = index, oldLine = oldLine, dependencies = dependencies, isDeclaration = true)

private fun MutableList<String>.replacePluginApplication(
    index: Int,
    oldLine: String,
    dependencies: List<Dependency>
) =
    replacePlugin(
        index = index, oldLine = oldLine, dependencies = dependencies, isDeclaration = false)

private fun MutableList<String>.replacePlugin(
    index: Int,
    oldLine: String,
    dependencies: List<Dependency>,
    isDeclaration: Boolean
) {
  val plugin = dependencies.firstPluginMatching(oldLine) ?: return
  var oldDeclarationRegex = """id\("${plugin.getIdentifier()}"\)"""
  if (isDeclaration) oldDeclarationRegex += """ version[ \(]"[^"]*"[\)]?"""
  val newDeclaration = "alias(libs.plugins.${plugin.toGradleReference()})"
  val newLine = oldLine.replace(oldDeclarationRegex.toRegex(), newDeclaration)
  set(index, newLine)
}

private fun Dependency.toGradleReference() = getIdentifier().replace("-", ".")

private fun String.isLibrary() = containsAny(GradleKtsConstants.supportedLibraryDeclarations)

private fun String.isPluginDeclaration() =
    containsAny(GradleKtsConstants.supportedPluginDeclarations)

private fun String.isPluginApplication() =
    containsAny(GradleKtsConstants.supportedPluginApplications)

private fun List<Dependency>.firstLibraryMatching(line: String): Dependency.Library? =
    longestDependencyMatching(line)

private fun List<Dependency>.firstPluginMatching(line: String): Dependency.Plugin? =
    longestDependencyMatching(line)

private inline fun <reified R : Dependency> List<Dependency>.longestDependencyMatching(
    line: String
): R? =
    filterIsInstance<R>()
        .filter { line.contains(it.getReference()) }
        .maxByOrNull { it.getReference().length }
