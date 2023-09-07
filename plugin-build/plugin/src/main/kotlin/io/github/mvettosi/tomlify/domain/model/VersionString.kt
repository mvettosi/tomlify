package io.github.mvettosi.tomlify.domain.model

@JvmInline
value class VersionString(val version: String) : Comparable<VersionString> {
  override fun compareTo(other: VersionString): Int =
      if (version.isPrefixOf(other.version)) 1 else version.compareTo(other.version)

  override fun toString() = version

  fun isBlank(): Boolean = version.isBlank()
  fun isNotBlank(): Boolean = version.isNotBlank()
  private fun String.isPrefixOf(other: String) = other.startsWith(this)
}
