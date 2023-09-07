package io.github.mvettosi.tomlify.domain.usecase.extract

import io.github.mvettosi.tomlify.domain.model.Dependency
import io.github.mvettosi.tomlify.domain.model.GradleKtsConstants
import io.github.mvettosi.tomlify.domain.model.VersionString
import io.github.mvettosi.tomlify.domain.model.containsAny
import io.github.mvettosi.tomlify.domain.source.GradleSource

class ExtractGradleKts(private val gradleSource: GradleSource) {
  operator fun invoke(): List<Dependency> =
      buildMap {
            gradleSource.readFiles { lines ->
              lines
                  .filter { line -> line.containsAny(GradleKtsConstants.supportedDeclarations) }
                  .filterNot { line -> line.containsAny(GradleKtsConstants.libraryExclusions) }
                  .filterNot { line -> line.trim().startsWith("//") }
                  .forEach { line -> extractDependency(line) }
            }
          }
          .values
          .toList()

  private fun MutableMap<String, Dependency>.extractDependency(line: String) {
    when {
      line.containsAny(GradleKtsConstants.supportedLibraryDeclarations) -> {
        val fields = extractLibrary(line).split(":")
        val fullVersionString = fields.getOrElse(2) { "" }.split("@")
        val actualVersionString = fullVersionString[0]
        val selector = fullVersionString.getOrNull(1)
        if (fields.size > 1) {
          addIfNewer(
              value =
                  Dependency.Library(
                      group = fields[0],
                      name = fields[1],
                      version = VersionString(actualVersionString),
                      selector = selector))
        }
      }
      line.containsAny(GradleKtsConstants.supportedPluginDeclarations) -> {
        addIfNewer(
            value =
                Dependency.Plugin(id = extractPluginId(line), version = extractPluginVersion(line)))
      }
    }
  }
  private fun MutableMap<String, Dependency>.addIfNewer(value: Dependency) {
    val key =
        when (value) {
          is Dependency.Library -> "${value.group}-${value.name}"
          is Dependency.Plugin -> value.id
        }
    if (listOf(key, value.getReference(), value.getIdentifier()).any { it.isBlank() }) return
    val newVersion = value.version
    val existingVersion = getOrDefault(key = key, defaultValue = null)?.version
    if (existingVersion == null || newVersion > existingVersion) {
      put(key, value)
    }
  }

  private fun extractPluginId(string: String) = extractField(string, "id")
  private fun extractPluginVersion(string: String) = VersionString(extractField(string, "version"))
  private fun extractLibrary(string: String): String {
    val keys = GradleKtsConstants.supportedLibraryDeclarations.map { it.replace("(", "") }
    val keySelection = keys.joinToString(separator = "|", prefix = "[", postfix = "]")
    return extractField(string, keySelection)
  }

  private fun extractField(string: String, field: String): String {
    val regex = Regex("$field[( ]\"(.*?)\"[)]?")
    val match = regex.find(string)
    return match?.groups?.get(1)?.value ?: ""
  }
}
