package io.github.mvettosi.tomlify

import java.io.File
import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.provider.Property

@Suppress("UnnecessaryAbstractClass")
abstract class TomlifyExtension @Inject constructor(project: Project) {

  private val objects = project.objects

  //  // Example of a property that is mandatory. The task will
  //  // fail if this property is not set as is annotated with @Optional.
  //  val message: Property<String> = objects.property(String::class.java)
  //
  //  // Example of a property that is optional.
  //  val tag: Property<String> = objects.property(String::class.java)

  private val defaultCatalog = File(File(project.rootDir, "gradle"), "libs.versions.toml")
  val catalogLocation: Property<String> =
      objects.property(String::class.java).convention(defaultCatalog.absolutePath)
}
