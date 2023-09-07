package io.github.mvettosi.tomlify.data.gradle

import io.github.mvettosi.tomlify.domain.source.GradleSource
import kotlin.io.path.writeLines
import org.gradle.api.Project

class GradleKtsSource(project: Project) : GradleSource {
  private val ktsFiles = project.rootDir.walk().filter { it.name.endsWith("kts") }

  override fun readFiles(read: (List<String>) -> Unit) {
    ktsFiles.forEach { read(it.readLines()) }
  }

  override fun writeFiles(transform: MutableList<String>.() -> Unit) {
    ktsFiles.forEach {
      val lines = it.readLines().toMutableList()
      lines.transform()
      it.toPath().writeLines(lines)
    }
  }

  // TODO move into use case and delete
  fun replaceReferences(changeMap: Map<String, String>) {
    ktsFiles.forEach { file ->
      var text = file.readText()
      changeMap.entries.forEach { entry -> text = text.replace(Regex(entry.key), entry.value) }
      file.writeText(text)
    }
  }
}
