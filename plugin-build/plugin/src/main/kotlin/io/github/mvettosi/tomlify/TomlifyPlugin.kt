package io.github.mvettosi.tomlify

import io.github.mvettosi.tomlify.di.AppInjection
import org.gradle.api.Plugin
import org.gradle.api.Project

const val EXTENSION_NAME = "tomlify"
const val TASK_NAME = "tomlify"

abstract class TomlifyPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    // Add the 'tomlify' extension object
    val extension = project.extensions.create(EXTENSION_NAME, TomlifyExtension::class.java, project)

    // Initialise dependency injection
    AppInjection.init(project = project, tomlLocation = extension.catalogLocation)

    // Add a task
    project.tasks.register(TASK_NAME, TomlifyTask::class.java)
  }
}
