package io.github.mvettosi.tomlify.domain.usecase.extract

import io.github.mvettosi.tomlify.domain.model.Dependency
import io.github.mvettosi.tomlify.domain.source.GradleSource
import kotlin.test.assertEquals
import kotlin.test.assertIs
import org.junit.jupiter.api.Test

class ExtractGradleKtsTest {

  @Test
  fun `check extracting all types of library references`() {
    // Arrange
    val lines =
        listOf(
            "    implementation(\"one:one:1.1.1\")",
            "    api(\"two:two:2.2.2\")",
            "    compile(\"three:three:3.3.3\")",
            "    kapt(\"four:four:4.4.4\")",
            "    ksp(\"five:five:5.5.5\")",
            "    implementation(\"six:six:6.6.6@aar\")",
        )
    val underTest = ExtractGradleKts(TestGradleSource(lines))

    // Act
    val actual = underTest()

    // Assert
    assertEquals(expected = 6, actual = actual.size)
    println(actual)
    assertIs<List<Dependency.Library>>(actual)
    assertEquals(expected = "one", actual = actual[0].group)
    assertEquals(expected = "one", actual = actual[0].name)
    assertEquals(expected = "1.1.1", actual = actual[0].version.version)
    assertEquals(expected = null, actual = actual[0].selector)

    assertEquals(expected = "two", actual = actual[1].group)
    assertEquals(expected = "two", actual = actual[1].name)
    assertEquals(expected = "2.2.2", actual = actual[1].version.version)
    assertEquals(expected = null, actual = actual[1].selector)

    assertEquals(expected = "three", actual = actual[2].group)
    assertEquals(expected = "three", actual = actual[2].name)
    assertEquals(expected = "3.3.3", actual = actual[2].version.version)
    assertEquals(expected = null, actual = actual[2].selector)

    assertEquals(expected = "four", actual = actual[3].group)
    assertEquals(expected = "four", actual = actual[3].name)
    assertEquals(expected = "4.4.4", actual = actual[3].version.version)
    assertEquals(expected = null, actual = actual[3].selector)

    assertEquals(expected = "five", actual = actual[4].group)
    assertEquals(expected = "five", actual = actual[4].name)
    assertEquals(expected = "5.5.5", actual = actual[4].version.version)
    assertEquals(expected = null, actual = actual[4].selector)

    assertEquals(expected = "six", actual = actual[5].group)
    assertEquals(expected = "six", actual = actual[5].name)
    assertEquals(expected = "6.6.6", actual = actual[5].version.version)
    assertEquals(expected = "aar", actual = actual[5].selector)
  }

  @Test
  fun `check newer library is replaced`() {
    // Arrange
    val lines =
        listOf(
            "    implementation(\"one:one:1.1.1\")",
            "    api(\"one:one:2.2.2\")",
        )
    val underTest = ExtractGradleKts(TestGradleSource(lines))

    // Act
    val actual = underTest()

    // Assert
    assertEquals(expected = 1, actual = actual.size)
    println(actual)
    assertIs<List<Dependency.Library>>(actual)
    assertEquals(expected = "one", actual = actual[0].group)
    assertEquals(expected = "one", actual = actual[0].name)
    assertEquals(expected = "2.2.2", actual = actual[0].version.version)
  }

  @Test
  fun `check unstable library is replaced`() {
    // Arrange
    val lines =
        listOf(
            "    implementation(\"one:one:1.1.1-beta\")",
            "    api(\"one:one:1.1.1\")",
        )
    val underTest = ExtractGradleKts(TestGradleSource(lines))

    // Act
    val actual = underTest()

    // Assert
    assertEquals(expected = 1, actual = actual.size)
    println(actual)
    assertIs<List<Dependency.Library>>(actual)
    assertEquals(expected = "one", actual = actual[0].group)
    assertEquals(expected = "one", actual = actual[0].name)
    assertEquals(expected = "1.1.1", actual = actual[0].version.version)
  }

  @Test
  fun `check unsupported formats are ignored`() {
    // Arrange
    val lines =
        listOf(
            "    implementation(project(\":temp\"))",
            "    implementation(kotlin(\"stdlib\"))",
            "    api(\"one:one:1.1.1\")",
        )
    val underTest = ExtractGradleKts(TestGradleSource(lines))

    // Act
    val actual = underTest()

    // Assert
    assertEquals(expected = 1, actual = actual.size)
    println(actual)
    assertIs<List<Dependency.Library>>(actual)
    assertEquals(expected = "one", actual = actual[0].group)
    assertEquals(expected = "one", actual = actual[0].name)
    assertEquals(expected = "1.1.1", actual = actual[0].version.version)
  }

  @Test
  fun `check commented lines are ignored`() {
    val lines =
        listOf(
            "    implementation(\"one:one:1.1.1\")",
            "    api(\"two:two:2.2.2\")",
            "    compile(\"three:three:3.3.3\")",
            "    //kapt(\"four:four:4.4.4\")",
            "  //  ksp(\"five:five:5.5.5\")",
            "//    implementation(\"six:six:6.6.6@aar\")",
        )
    val underTest = ExtractGradleKts(TestGradleSource(lines))

    // Act
    val actual = underTest()

    // Assert
    assertEquals(expected = 3, actual = actual.size)
    assertIs<List<Dependency.Library>>(actual)
    assertEquals(expected = "one", actual = actual[0].group)
    assertEquals(expected = "one", actual = actual[0].name)
    assertEquals(expected = "1.1.1", actual = actual[0].version.version)
    assertEquals(expected = null, actual = actual[0].selector)

    assertEquals(expected = "two", actual = actual[1].group)
    assertEquals(expected = "two", actual = actual[1].name)
    assertEquals(expected = "2.2.2", actual = actual[1].version.version)
    assertEquals(expected = null, actual = actual[1].selector)

    assertEquals(expected = "three", actual = actual[2].group)
    assertEquals(expected = "three", actual = actual[2].name)
    assertEquals(expected = "3.3.3", actual = actual[2].version.version)
    assertEquals(expected = null, actual = actual[2].selector)
  }

  @Test
  fun `check skipped lines are ignored`() {
    val lines =
        listOf(
            "    implementation(\"one:one:1.1.1\")",
            "    api(\"two:two:2.2.2\")",
            "    compile(\"three:three:3.3.3\")",
            "    kapt(\"four:four:4.4.4\") //no-tomlify",
            "    ksp(\"five:five:5.5.5\") //no-tomlify",
            "    implementation(\"six:six:6.6.6@aar\") //no-tomlify",
        )
    val underTest = ExtractGradleKts(TestGradleSource(lines))

    // Act
    val actual = underTest()

    // Assert
    assertEquals(expected = 3, actual = actual.size)
    assertIs<List<Dependency.Library>>(actual)
    assertEquals(expected = "one", actual = actual[0].group)
    assertEquals(expected = "one", actual = actual[0].name)
    assertEquals(expected = "1.1.1", actual = actual[0].version.version)
    assertEquals(expected = null, actual = actual[0].selector)

    assertEquals(expected = "two", actual = actual[1].group)
    assertEquals(expected = "two", actual = actual[1].name)
    assertEquals(expected = "2.2.2", actual = actual[1].version.version)
    assertEquals(expected = null, actual = actual[1].selector)

    assertEquals(expected = "three", actual = actual[2].group)
    assertEquals(expected = "three", actual = actual[2].name)
    assertEquals(expected = "3.3.3", actual = actual[2].version.version)
    assertEquals(expected = null, actual = actual[2].selector)
  }

  @Test
  fun `check extracting all supported syntax of plugin references`() {
    // Arrange
    val lines =
        listOf(
            "    id(\"one\") version \"1.1.1\"",
            "    id(\"one\")",
            "    id(\"two\") version(\"2.2.2\")",
            "    id(\"two\")",
        )
    val underTest = ExtractGradleKts(TestGradleSource(lines))

    // Act
    val actual = underTest()

    // Assert
    assertEquals(expected = 2, actual = actual.size)
    println(actual)
    assertIs<List<Dependency.Plugin>>(actual)
    assertEquals(expected = "one", actual = actual[0].id)
    assertEquals(expected = "1.1.1", actual = actual[0].version.version)

    assertEquals(expected = "two", actual = actual[1].id)
    assertEquals(expected = "2.2.2", actual = actual[1].version.version)
  }

  private class TestGradleSource(val lines: List<String>) : GradleSource {
    override fun readFiles(read: (List<String>) -> Unit) = read(lines)
    override fun writeFiles(transform: MutableList<String>.() -> Unit) {}
  }
}
