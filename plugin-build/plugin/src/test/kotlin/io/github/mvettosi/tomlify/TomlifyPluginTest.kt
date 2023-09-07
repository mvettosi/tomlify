package io.github.mvettosi.tomlify

import kotlin.test.Test
import kotlin.test.assertNotNull
import org.gradle.testfixtures.ProjectBuilder

class TomlifyPluginTest {

  @Test
  fun `plugin is applied correctly to the project`() {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("io.github.mvettosi.tomlify")

    assert(project.tasks.getByName(TASK_NAME) is TomlifyTask)
  }

  @Test
  fun `extension is created correctly`() {
    val project = ProjectBuilder.builder().build()
    project.pluginManager.apply("io.github.mvettosi.tomlify")

    assertNotNull(project.extensions.getByName(EXTENSION_NAME))
  }

  //  @Test
  //  fun `parameters are passed correctly from extension to task`() {
  //    val project = ProjectBuilder.builder().build()
  //    project.pluginManager.apply("io.github.mvettosi.tomlify")
  //    val aFile = File(project.projectDir, ".tmp")
  //    (project.extensions.getByName(EXTENSION_NAME) as TomlifyExtension).apply {
  ////      tag.set("a-sample-tag")
  ////      message.set("just-a-message")
  //      catalogLocation.set(aFile)
  //    }
  //
  //    val task = project.tasks.getByName(TASK_NAME) as TomlifyTask
  //
  ////    assertEquals("a-sample-tag", task.tag.get())
  ////    assertEquals("just-a-message", task.message.get())
  ////    assertEquals(aFile, task.catalogLocation.get().asFile)
  //  }
}
