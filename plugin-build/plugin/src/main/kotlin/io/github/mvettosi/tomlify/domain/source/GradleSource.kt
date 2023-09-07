package io.github.mvettosi.tomlify.domain.source

interface GradleSource {
  fun readFiles(read: (List<String>) -> Unit)

  fun writeFiles(transform: MutableList<String>.() -> Unit)
}
