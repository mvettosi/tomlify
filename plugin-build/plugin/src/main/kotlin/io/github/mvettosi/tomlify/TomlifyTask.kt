package io.github.mvettosi.tomlify

import io.github.mvettosi.tomlify.di.AppInjection
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.TaskAction

abstract class TomlifyTask : DefaultTask() {

  init {
    description = "Base plugin task to move all legacy dependency references to the TOML catalog"
    group = BasePlugin.BUILD_GROUP
  }

  //  @get:Input
  //  @get:Option(option = "message", description = "A message to be printed in the output file")
  //  abstract val message: Property<String>
  //
  //  @get:Input
  //  @get:Option(option = "tag", description = "A Tag to be used for debug and in the output file")
  //  @get:Optional
  //  abstract val tag: Property<String>

  //  @get:OutputFile
  //  @get:Optional
  //  abstract val catalogLocation: RegularFileProperty

  @TaskAction
  fun tomlifyAction() {
    AppInjection.tomlify()
    //    val prettyTag = tag.orNull?.let { "[$it]" } ?: ""
    //
    //    logger.lifecycle("$prettyTag message is: ${message.orNull}")
    //    logger.lifecycle("$prettyTag tag is: ${tag.orNull}")
    //    logger.lifecycle("$prettyTag outputFile is: ${catalogLocation.orNull}")
    //
    //    catalogLocation.get().asFile.writeText("$prettyTag ${message.get()}")
  }
}
