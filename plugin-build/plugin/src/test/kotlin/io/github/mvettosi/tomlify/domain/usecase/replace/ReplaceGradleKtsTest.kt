package io.github.mvettosi.tomlify.domain.usecase.replace

import io.github.mvettosi.tomlify.domain.model.Dependency
import io.github.mvettosi.tomlify.domain.model.VersionString
import io.github.mvettosi.tomlify.domain.source.GradleSource
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class ReplaceGradleKtsTest {
  @Test
  fun `check replacing works as expected`() {
    // Arrange
    val dependencies =
        listOf(
            Dependency.Plugin(id = "com.android.application", version = VersionString("1.2.3")),
            Dependency.Library(
                group = "library.without", name = "version", version = VersionString("")),
            Dependency.Library(
                group = "androidx.appcompat", name = "appcompat", version = VersionString("1.2.3")),
            Dependency.Library(
                group = "androidx.appcompat",
                name = "appcompat.something",
                version = VersionString("1.2.3")),
            Dependency.Library(
                group = "one.two",
                name = "three.four",
                version = VersionString("1.2.3"),
                selector = "someSelector"),
        )
    val testGradleSource = TestGradleSource()
    val underTest = ReplaceGradkeKts(testGradleSource)

    // Act
    underTest(dependencies)

    // Assert
    assertEquals(expected = EXPECTED, actual = testGradleSource.gradle)
  }

  private class TestGradleSource : GradleSource {
    var gradle = TEST_GRADLE_KTS
    override fun readFiles(read: (List<String>) -> Unit) {}
    override fun writeFiles(transform: MutableList<String>.() -> Unit) {
      val mutableGradle = gradle.lines().toMutableList()
      mutableGradle.transform()
      gradle = mutableGradle.joinToString("\n")
    }

    private companion object {
      val TEST_GRADLE_KTS =
          """
          id("com.android.application")
          id("com.android.application") version("1.2.3")
          id("com.android.application") version "1.2.3"
          id("com.android.application") version("1.2.3") apply false
          id("com.android.application") version "1.2.3" apply false
          implementation("library.without:version")
          implementation("androidx.appcompat:appcompat:1.2.3")
          implementation("androidx.appcompat:appcompat.something:1.2.3")
          implementation("one.two:three.four:1.2.3@someSelector")
        """
              .trimIndent()
    }
  }

  private companion object {
    val EXPECTED =
        """
        alias(libs.plugins.com.android.application)
        alias(libs.plugins.com.android.application)
        alias(libs.plugins.com.android.application)
        alias(libs.plugins.com.android.application) apply false
        alias(libs.plugins.com.android.application) apply false
        implementation(libs.library.without.version)
        implementation(libs.androidx.appcompat.appcompat)
        implementation(libs.androidx.appcompat.appcompat.something)
        implementation(libs.one.two.three.four) { artifact { type = "someSelector" } }
      """
            .trimIndent()
  }
}
