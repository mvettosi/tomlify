package io.github.mvettosi.tomlify.domain.model

sealed class Dependency(open val version: VersionString) {
  abstract fun getIdentifier(): String
  abstract fun getReference(): String

  data class Library(
      val group: String,
      val name: String,
      override val version: VersionString,
      val selector: String? = null
  ) : Dependency(version = version) {
    override fun getIdentifier() = "$group-$name"
    override fun getReference() = "$group:$name"
  }

  data class Plugin(val id: String, override val version: VersionString) :
      Dependency(version = version) {
    override fun getIdentifier() = id
    override fun getReference() = id
  }
}
