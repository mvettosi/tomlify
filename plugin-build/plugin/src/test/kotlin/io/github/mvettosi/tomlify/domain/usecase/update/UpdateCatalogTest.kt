package io.github.mvettosi.tomlify.domain.usecase.update

import io.github.mvettosi.tomlify.domain.model.Dependency
import io.github.mvettosi.tomlify.domain.model.TomlHeader
import io.github.mvettosi.tomlify.domain.model.VersionString
import io.github.mvettosi.tomlify.domain.source.CatalogSource
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateCatalogTest {
  private val testCatalogSource = TestCatalogSource()
  private val underTest = UpdateCatalog(testCatalogSource, SetupCatalog(testCatalogSource))

  @BeforeEach
  fun setUp() {
    testCatalogSource.clear()
  }

  @Test
  fun `check first library is added correctly`() {
    // Arrange
    val dependencies =
        listOf(
            Dependency.Library(group = "one.two", name = "three", version = VersionString("1.1.1")))
    val expected =
        """
        ${TomlHeader.VERSIONS}
        one-two-three-version = "1.1.1"

        ${TomlHeader.BUNDLES}

        ${TomlHeader.LIBRARIES}
        one-two-three = { group = "one.two", name = "three", version.ref = "one-two-three-version" }

        ${TomlHeader.PLUGINS}
      """
            .trimIndent()

    // Act
    underTest(dependencies)

    // Assert
    assertEquals(expected = expected, actual = testCatalogSource.catalog)
  }

  @Test
  fun `check second library is added correctly`() {
    // Arrange
    testCatalogSource.catalog =
        """
        ${TomlHeader.VERSIONS}
        one-two-three-version = "1.1.1"

        ${TomlHeader.BUNDLES}

        ${TomlHeader.LIBRARIES}
        one-two-three = { group = "one.two", name = "three", version.ref = "one-two-three-version" }

        ${TomlHeader.PLUGINS}
      """
            .trimIndent()
    val dependencies =
        listOf(
            Dependency.Library(group = "four.five", name = "six", version = VersionString("2.2.2")))
    val expected =
        """
        ${TomlHeader.VERSIONS}
        one-two-three-version = "1.1.1"
        four-five-six-version = "2.2.2"

        ${TomlHeader.BUNDLES}

        ${TomlHeader.LIBRARIES}
        one-two-three = { group = "one.two", name = "three", version.ref = "one-two-three-version" }
        four-five-six = { group = "four.five", name = "six", version.ref = "four-five-six-version" }

        ${TomlHeader.PLUGINS}
      """
            .trimIndent()

    // Act
    underTest(dependencies)

    // Assert
    assertEquals(expected = expected, actual = testCatalogSource.catalog)
  }

  @Test
  fun `check library version update`() {
    // Arrange
    testCatalogSource.catalog =
        """
        ${TomlHeader.VERSIONS}
        one-two-three-version = "1.1.1"
        four-five-six-version = "2.2.2"

        ${TomlHeader.BUNDLES}

        ${TomlHeader.LIBRARIES}
        one-two-three = { group = "one.two", name = "three", version.ref = "one-two-three-version" }
        four-five-six = { group = "four.five", name = "six", version.ref = "four-five-six-version" }

        ${TomlHeader.PLUGINS}
      """
            .trimIndent()
    val dependencies =
        listOf(
            Dependency.Library(group = "one.two", name = "three", version = VersionString("1.1.2")))
    val expected =
        """
        ${TomlHeader.VERSIONS}
        one-two-three-version = "1.1.2"
        four-five-six-version = "2.2.2"

        ${TomlHeader.BUNDLES}

        ${TomlHeader.LIBRARIES}
        one-two-three = { group = "one.two", name = "three", version.ref = "one-two-three-version" }
        four-five-six = { group = "four.five", name = "six", version.ref = "four-five-six-version" }

        ${TomlHeader.PLUGINS}
      """
            .trimIndent()

    // Act
    underTest(dependencies)

    // Assert
    assertEquals(expected = expected, actual = testCatalogSource.catalog)
  }

  @Test
  fun `check first plugin is added correctly`() {
    // Arrange
    val dependencies =
        listOf(Dependency.Plugin(id = "one.two.three", version = VersionString("1.1.1")))
    val expected =
        """
        ${TomlHeader.VERSIONS}
        one-two-three-version = "1.1.1"

        ${TomlHeader.BUNDLES}

        ${TomlHeader.LIBRARIES}

        ${TomlHeader.PLUGINS}
        one-two-three = { id = "one.two.three", version.ref = "one-two-three-version" }
      """
            .trimIndent()

    // Act
    underTest(dependencies)

    // Assert
    assertEquals(expected = expected, actual = testCatalogSource.catalog)
  }

  @Test
  fun `check second plugin is added correctly`() {
    // Arrange
    testCatalogSource.catalog =
        """
        ${TomlHeader.VERSIONS}
        one-two-three-version = "1.1.1"
        four-five-six-version = "2.2.2"
        seven-eight-version = "3.3.3"

        ${TomlHeader.BUNDLES}

        ${TomlHeader.LIBRARIES}
        one-two-three = { group = "one.two", name = "three", version.ref = "one-two-three-version" }
        four-five-six = { group = "four.five", name = "six", version.ref = "four-five-six-version" }

        ${TomlHeader.PLUGINS}
        seven-eight = { id = "seven.eight", version.ref = "seven-eight-version" }
      """
            .trimIndent()
    val dependencies = listOf(Dependency.Plugin(id = "nine.ten", version = VersionString("4.4.4")))
    val expected =
        """
        ${TomlHeader.VERSIONS}
        one-two-three-version = "1.1.1"
        four-five-six-version = "2.2.2"
        seven-eight-version = "3.3.3"
        nine-ten-version = "4.4.4"

        ${TomlHeader.BUNDLES}

        ${TomlHeader.LIBRARIES}
        one-two-three = { group = "one.two", name = "three", version.ref = "one-two-three-version" }
        four-five-six = { group = "four.five", name = "six", version.ref = "four-five-six-version" }

        ${TomlHeader.PLUGINS}
        seven-eight = { id = "seven.eight", version.ref = "seven-eight-version" }
        nine-ten = { id = "nine.ten", version.ref = "nine-ten-version" }
      """
            .trimIndent()

    // Act
    underTest(dependencies)

    // Assert
    assertEquals(expected = expected, actual = testCatalogSource.catalog)
  }

  @Test
  fun `check plugin version update`() {
    // Arrange
    testCatalogSource.catalog =
        """
        ${TomlHeader.VERSIONS}
        one-two-three-version = "1.1.1"
        four-five-six-version = "2.2.2"
        seven-eight-version = "3.3.3"
        nine-ten-version = "4.4.4"

        ${TomlHeader.BUNDLES}

        ${TomlHeader.LIBRARIES}
        one-two-three = { group = "one.two", name = "three", version.ref = "one-two-three-version" }
        four-five-six = { group = "four.five", name = "six", version.ref = "four-five-six-version" }

        ${TomlHeader.PLUGINS}
        seven-eight = { id = "seven.eight", version.ref = "seven-eight-version" }
        nine-ten = { id = "nine.ten", version.ref = "nine-ten-version" }
      """
            .trimIndent()
    val dependencies =
        listOf(Dependency.Plugin(id = "seven.eight", version = VersionString("3.4.3")))
    val expected =
        """
        ${TomlHeader.VERSIONS}
        one-two-three-version = "1.1.1"
        four-five-six-version = "2.2.2"
        seven-eight-version = "3.4.3"
        nine-ten-version = "4.4.4"

        ${TomlHeader.BUNDLES}

        ${TomlHeader.LIBRARIES}
        one-two-three = { group = "one.two", name = "three", version.ref = "one-two-three-version" }
        four-five-six = { group = "four.five", name = "six", version.ref = "four-five-six-version" }

        ${TomlHeader.PLUGINS}
        seven-eight = { id = "seven.eight", version.ref = "seven-eight-version" }
        nine-ten = { id = "nine.ten", version.ref = "nine-ten-version" }
      """
            .trimIndent()

    // Act
    underTest(dependencies)

    // Assert
    assertEquals(expected = expected, actual = testCatalogSource.catalog)
  }

  private class TestCatalogSource : CatalogSource {
    var catalog: String = ""
    override fun isCatalogConfigured(): Boolean = catalog.isNotBlank()
    override fun initialiseCatalog(content: String) {
      catalog = content
    }
    override fun updateCatalog(transform: MutableList<String>.() -> Unit) {
      val mutableCatalog = catalog.lines().toMutableList()
      mutableCatalog.transform()
      catalog = mutableCatalog.joinToString("\n")
    }
    fun clear() {
      catalog = ""
    }
  }
}
