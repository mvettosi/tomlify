package io.github.mvettosi.tomlify.domain.model

sealed class GradleConstants(
    val supportedLibraryDeclarations: List<String>,
    val libraryExclusions: List<String>,
    val supportedPluginDeclarations: List<String>,
    val supportedPluginApplications: List<String>,
) {
  val supportedDeclarations
    get() = supportedLibraryDeclarations + supportedPluginDeclarations
}

object GradleKtsConstants :
    GradleConstants(
        supportedLibraryDeclarations =
            listOf(
                "mplementation(",
                "pi(",
                "ompile(",
                "kapt(",
                "ksp(",
                "coreLibraryDesugaring(",
            ),
        libraryExclusions = listOf("project(", "//no-tomlify"),
        supportedPluginDeclarations = listOf(" version(", " version "),
        supportedPluginApplications = listOf("id("))

object GradleGroovyConstants :
    GradleConstants(
        supportedLibraryDeclarations = listOf(),
        libraryExclusions = listOf("//no-tomlify"),
        supportedPluginDeclarations = listOf(),
        supportedPluginApplications = listOf())

fun String.containsAny(others: Collection<String>) = others.any { this.contains(it) }
